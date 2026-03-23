package org.instagram.user_service.controller;

import org.instagram.user_service.dto.CreateUserRequest;
import org.instagram.user_service.dto.UserResponse;
import org.instagram.user_service.service.UserService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PostMapping("/{id}/follow")
    public void follow(@PathVariable UUID id,
                       @RequestHeader("X-User-Id") UUID followerId) {
        userService.followUser(followerId, id);
    }

    @DeleteMapping("/{id}/follow")
    public void unfollow(@PathVariable UUID id,
                         @RequestHeader("X-User-Id") UUID followerId) {
        userService.unfollowUser(followerId, id);
    }

    @GetMapping("/{id}/followers")
    public List<UserResponse> followers(@PathVariable UUID id) {
        return userService.getFollowers(id);
    }

    @GetMapping("/{id}/following")
    public List<UserResponse> following(@PathVariable UUID id) {
        return userService.getFollowing(id);
    }
}