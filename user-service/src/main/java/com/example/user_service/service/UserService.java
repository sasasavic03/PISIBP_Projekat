package org.instagram.user_service.service;

import org.instagram.user_service.dto.CreateUserRequest;
import org.instagram.user_service.dto.UserResponse;
import org.instagram.user_service.entity.Follow;
import org.instagram.user_service.entity.User;
import org.instagram.user_service.repository.FollowRepository;
import org.instagram.user_service.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public UserService(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return mapToResponse(user);
    }

    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    public void followUser(UUID followerId, UUID followingId) {

        if (followerId.equals(followingId)) {
            throw new RuntimeException("Cannot follow yourself");
        }

        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new RuntimeException("Already following");
        }

        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        follow.setCreatedAt(LocalDateTime.now());

        followRepository.save(follow);
    }

    public void unfollowUser(UUID followerId, UUID followingId) {
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    public List<UserResponse> getFollowers(UUID userId) {
        return followRepository.findByFollowingId(userId).stream()
                .map(f -> getUser(f.getFollowerId()))
                .toList();
    }

    public List<UserResponse> getFollowing(UUID userId) {
        return followRepository.findByFollowerId(userId).stream()
                .map(f -> getUser(f.getFollowingId()))
                .toList();
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setBio(user.getBio());
        response.setProfilePictureUrl(user.getProfilePictureUrl());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }
}