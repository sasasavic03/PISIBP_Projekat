package org.instagram.follow.controller;


import org.instagram.follow.dto.FollowRequestDto;
import org.instagram.follow.dto.FollowResponseDto;
import org.instagram.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService service;

    public FollowController(FollowService service){
        this.service= service;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String,String>> health(){
        return ResponseEntity.ok(Map.of(
                "status","UP",
                "service","follow-service"
        ));
    }

    @PostMapping("/{targetUserId}")
    public ResponseEntity<Map<String, String>> follow(
            @PathVariable Long targetUserId,
            @RequestHeader("X-User-Id") Long followerId,
            @RequestBody FollowRequestDto request){
        try {
            service.follow(followerId, targetUserId, request.isPrivate());
            return ResponseEntity.ok(Map.of("status", request.isPrivate() ? "requested" : "following"));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Vec postoji relacija")) {
                return ResponseEntity.ok(Map.of("status", "already_following"));
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<Map<String, String>> unfollow(
            @PathVariable Long targetUserId,
            @RequestHeader("X-User-Id") Long followerId){
        try {
            service.unfollow(followerId, targetUserId);
            return ResponseEntity.ok(Map.of("status", "unfollowed"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowResponseDto>> getFollowers(@PathVariable Long userId){
        return ResponseEntity.ok(service.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowResponseDto>> getFollowing(@PathVariable Long userId){
        return ResponseEntity.ok(service.getFollowing(userId));
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Map<String, Long>> getFollowersCount(@PathVariable Long userId){
        Long count = service.getFollowersCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Map<String, Long>> getFollowingCount(@PathVariable Long userId){
        Long count = service.getFollowingCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkFollowStatus(
            @RequestHeader("X-User-Id") Long followerId,
            @RequestParam Long followingId) {
        String status = service.getFollowStatus(followerId, followingId);
        return ResponseEntity.ok(Map.of(
                "following", status.equals("following"),
                "requested", status.equals("requested"),
                "status", status
        ));
    }

    @PostMapping("/accept")
    public ResponseEntity<Map<String, String>> acceptFollow(
            @RequestParam Long followerId,
            @RequestParam Long followingId) {
        service.acceptFollow(followerId, followingId);
        return ResponseEntity.ok(Map.of("status", "accepted"));
    }
}
