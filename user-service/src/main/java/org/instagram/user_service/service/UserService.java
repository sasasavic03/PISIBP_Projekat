package org.instagram.user_service.service;

import org.instagram.user_service.client.BlockServiceClient;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowServiceClient followServiceClient;
    private final BlockServiceClient blockServiceClient;
    private final AvatarService avatarService;

    public UserService(UserRepository userRepository, FollowServiceClient followServiceClient, 
                      BlockServiceClient blockServiceClient, AvatarService avatarService) {
        this.userRepository = userRepository;
        this.followServiceClient = followServiceClient;
        this.blockServiceClient = blockServiceClient;
        this.avatarService = avatarService;
    }

    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("User profile already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (request.getId() != null && userRepository.existsById(request.getId())) {
            throw new UserAlreadyExistsException("User profile ID already exists");
        }

        User user = new User();
        if (request.getId() != null) {
            user.setId(request.getId());
        }
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setProfilePictureUrl(avatarService.getDefaultAvatarUrl());
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

    public Page<UserSearchResultDTO> searchUsers(String query, Pageable pageable, Long userId) {
        if (query == null || query.trim().isEmpty()) {
            throw new InvalidUserDataException("Search query cannot be empty");
        }

        Page<User> results = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, pageable);
        
        // Map to DTO and filter out blocked users if userId is provided
        List<UserSearchResultDTO> dtoList = results.getContent().stream()
                .map(user -> new UserSearchResultDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getFullname(),
                        user.getProfilePictureUrl(),
                        user.isPrivate()
                ))
                .toList();
        
        // Filter out blocked users if userId is provided
        if (userId != null) {
            List<Long> blockedUserIds = getBlockedAndBlockingUserIds(userId);
            dtoList = dtoList.stream()
                    .filter(user -> !blockedUserIds.contains(user.getId()))
                    .toList();
        }
        
        // Create a new Page object with filtered content
        return new org.springframework.data.domain.PageImpl<>(
                dtoList,
                pageable,
                results.getTotalElements() - (results.getContent().size() - dtoList.size())
        );
    }

    private List<Long> getBlockedAndBlockingUserIds(Long userId) {
        try {
            List<Long> allBlockedIds = new java.util.ArrayList<>();
            
            // Get users that this user has blocked
            List<Long> blockedIds = blockServiceClient.getBlockedUserIds(userId);
            if (blockedIds != null) {
                allBlockedIds.addAll(blockedIds);
            }
            
            // Get users that have blocked this user (so we don't show them in search)
            List<Long> blockingMeIds = blockServiceClient.getBlockerIds(userId);
            if (blockingMeIds != null) {
                allBlockedIds.addAll(blockingMeIds);
            }
            
            return allBlockedIds;
        } catch (Exception e) {
            // If we can't get blocked users, just proceed without filtering
            return new java.util.ArrayList<>();
        }
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

        if (request.getFullname() != null) {
            user.setFullname(request.getFullname());
        }

        if (request.getIsPrivate() != null) {
            user.setPrivate(request.getIsPrivate());
        }

        userRepository.save(user);
        return mapToResponse(user);
    }

    public List<SuggestionDTO> getSuggestions(Long userId, int limit) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }

        try {
            List<Long> following = followServiceClient.getFollowingIds(userId);


            Pageable pageable = PageRequest.of(0, limit);
            Page<User> suggestedUsers = userRepository.findByIdNotAndIdNotIn(userId, following, pageable);

            // Convert to SuggestionDTO
            return suggestedUsers.getContent().stream()
                    .map(u -> new SuggestionDTO(u.getId(), u.getUsername(), u.getProfilePictureUrl(), 0))
                    .collect(Collectors.toList());

        } catch (Exception e) {

            Pageable pageable = PageRequest.of(0, limit);
            Page<User> randomUsers = userRepository.findByIdNotAndIdNotIn(userId, List.of(), pageable);
            return randomUsers.getContent().stream()
                    .map(u -> new SuggestionDTO(u.getId(), u.getUsername(), u.getProfilePictureUrl(), 0))
                    .collect(Collectors.toList());
        }
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullname(user.getFullname());
        response.setBio(user.getBio());
        response.setProfilePictureUrl(user.getProfilePictureUrl());
        response.setIsPrivate(user.isPrivate());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

    public UserResponse updatePrivacy(Long userId, Long currentUserId, Boolean isPrivate) {
        if (!userId.equals(currentUserId)) {
            throw new UnauthorizedException("You can only update your own profile");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPrivate(isPrivate);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        return mapToResponse(user);
    }
}

