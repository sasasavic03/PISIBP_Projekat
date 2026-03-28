package org.instagram.user_service.service;

import org.instagram.user_service.client.FollowServiceClient;
import org.instagram.user_service.dto.CreateUserRequest;
import org.instagram.user_service.dto.SuggestionDTO;
import org.instagram.user_service.dto.UpdateProfileRequest;
import org.instagram.user_service.dto.UserResponse;
import org.instagram.user_service.dto.UserSearchResultDTO;
import org.instagram.user_service.entity.User;
import org.instagram.user_service.exception.InvalidUserDataException;
import org.instagram.user_service.exception.UnauthorizedException;
import org.instagram.user_service.exception.UserAlreadyExistsException;
import org.instagram.user_service.exception.UserNotFoundException;
import org.instagram.user_service.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowServiceClient followServiceClient;

    public UserService(UserRepository userRepository, FollowServiceClient followServiceClient) {
        this.userRepository = userRepository;
        this.followServiceClient = followServiceClient;
    }

    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("User profile already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setId(request.getId());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return mapToResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        return mapToResponse(user);
    }

    public Page<UserSearchResultDTO> searchUsers(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            throw new InvalidUserDataException("Search query cannot be empty");
        }

        Page<User> results = userRepository.searchUsers(query, pageable);
        return results.map(user -> new UserSearchResultDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getProfilePictureUrl(),
            user.getIsPrivate()
        ));
    }

    public UserResponse updateUser(Long userId, Long currentUserId, UpdateProfileRequest request) {
        if (!userId.equals(currentUserId)) {
            throw new UnauthorizedException("You can only update your own profile");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        if (request.getIsPrivate() != null) {
            user.setIsPrivate(request.getIsPrivate());
        }

        userRepository.save(user);
        return mapToResponse(user);
    }

    public List<SuggestionDTO> getSuggestions(Long userId, int limit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        try {
            List<Long> following = followServiceClient.getFollowingIds(userId);
            List<Long> followers = followServiceClient.getFollowerIds(userId);
            
            List<User> allUsers = userRepository.findAll();
            
            return allUsers.stream()
                    .filter(u -> !u.getId().equals(userId))
                    .filter(u -> !following.contains(u.getId()))
                    .map(u -> {
                        int mutualCount = (int) followers.stream()
                                .filter(followerId -> {
                                    try {
                                        List<Long> userFollowers = followServiceClient.getFollowerIds(u.getId());
                                        return userFollowers.contains(followerId);
                                    } catch (Exception e) {
                                        return false;
                                    }
                                })
                                .count();
                        return new SuggestionDTO(u.getId(), u.getUsername(), u.getProfilePictureUrl(), mutualCount);
                    })
                    .sorted((a, b) -> Integer.compare(b.getMutualFollowers(), a.getMutualFollowers()))
                    .limit(limit)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // If follow-service is unavailable, return random users not followed
            List<User> randomUsers = userRepository.findAll();
            try {
                List<Long> following = followServiceClient.getFollowingIds(userId);
                return randomUsers.stream()
                        .filter(u -> !u.getId().equals(userId))
                        .filter(u -> !following.contains(u.getId()))
                        .limit(limit)
                        .map(u -> new SuggestionDTO(u.getId(), u.getUsername(), u.getProfilePictureUrl(), 0))
                        .collect(Collectors.toList());
            } catch (Exception ex) {
                return new ArrayList<>();
            }
        }
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setBio(user.getBio());
        response.setProfilePictureUrl(user.getProfilePictureUrl());
        response.setIsPrivate(user.getIsPrivate());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }
}