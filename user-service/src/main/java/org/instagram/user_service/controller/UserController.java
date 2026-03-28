package org.instagram.user_service.controller;

import org.instagram.user_service.dto.CreateUserRequest;
import org.instagram.user_service.dto.SuggestionDTO;
import org.instagram.user_service.dto.UpdateProfileRequest;
import org.instagram.user_service.dto.UserResponse;
import org.instagram.user_service.dto.UserSearchResultDTO;
import org.instagram.user_service.service.UserService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse response = userService.getUserByUsername(username);
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

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
        @PathVariable Long id,
        @AuthenticationPrincipal Long currentUserId,
        @RequestBody UpdateProfileRequest request) {

    return ResponseEntity.ok(userService.updateUser(id, currentUserId, request));
    }

    @GetMapping("/{id}/suggestions")
    public ResponseEntity<List<SuggestionDTO>> getSuggestions(
            @PathVariable Long id,
            @RequestParam(defaultValue = "10") int limit) {
        List<SuggestionDTO> suggestions = userService.getSuggestions(id, Math.min(limit, 50));
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            @AuthenticationPrincipal Long userId) {
    
        return ResponseEntity.ok(userService.getUser(userId));
    }
}
