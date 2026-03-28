package org.instagram.user_service.controller;

import org.instagram.user_service.client.FollowServiceClient;
import org.instagram.user_service.dto.CreateUserRequest;
import org.instagram.user_service.dto.SuggestionDTO;
import org.instagram.user_service.dto.UpdateProfileRequest;
import org.instagram.user_service.dto.UserResponse;
import org.instagram.user_service.dto.UserSearchResultDTO;
import org.instagram.user_service.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RestTemplate restTemplate;
    private final FollowServiceClient followServiceClient;

    private static final String BLOCK_SERVICE_URL = "http://block-service:8080/api/users";

    public UserController(UserService userService, RestTemplate restTemplate, FollowServiceClient followServiceClient) {
        this.userService = userService;
        this.restTemplate = restTemplate;
        this.followServiceClient = followServiceClient;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long currentUserId,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserResponse response = userService.updateUser(id, currentUserId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<UserResponse> updateUserSettings(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long currentUserId,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserResponse response = userService.updateUser(id, currentUserId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserSearchResultDTO>> searchUsers(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 50));
        Page<UserSearchResultDTO> results = userService.searchUsers(q, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<SuggestionDTO>> getUserSuggestions(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<SuggestionDTO> suggestions = userService.getSuggestions(userId, Math.min(limit, 50));
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}/stats")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable String username) {
        UserResponse user = userService.getUserByUsername(username);
        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", user.getId());
        stats.put("username", user.getUsername());
        stats.put("bio", user.getBio());
        stats.put("profilePictureUrl", user.getProfilePictureUrl());
        stats.put("postsCount", 0);

        // Fetch follower and following counts from follow-service
        try {
            List<Long> followers = followServiceClient.getFollowerIds(user.getId());
            List<Long> following = followServiceClient.getFollowingIds(user.getId());
            stats.put("followersCount", followers != null ? followers.size() : 0);
            stats.put("followingCount", following != null ? following.size() : 0);
        } catch (Exception e) {
            stats.put("followersCount", 0);
            stats.put("followingCount", 0);
        }

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}/suggestions")
    public ResponseEntity<List<SuggestionDTO>> getSuggestions(
            @PathVariable Long id,
            @RequestParam(defaultValue = "10") int limit) {
        List<SuggestionDTO> suggestions = userService.getSuggestions(id, Math.min(limit, 50));
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/{id}/blocked")
    public ResponseEntity<?> getBlockedUsers(@PathVariable Long id) {
        try {
            String url = BLOCK_SERVICE_URL + "/" + id + "/blocked";
            ResponseEntity<List<?>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch blocked users");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUser(id);
        return ResponseEntity.ok(response);
    }
}