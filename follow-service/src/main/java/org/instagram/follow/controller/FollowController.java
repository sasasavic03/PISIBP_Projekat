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
    public ResponseEntity<Map<String, String>> follow(@PathVariable Long targetUserId,
                                       @RequestBody FollowRequestDto request){
        service.follow(request.getFollowerId(),targetUserId, request.isPrivate());
        return ResponseEntity.ok(Map.of("message", "Successfully followed user"));
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<Map<String, String>> unfollow(@PathVariable Long targetUserId,
                                         @RequestBody FollowRequestDto request){
        service.unfollow(request.getFollowerId(),targetUserId);
        return ResponseEntity.ok(Map.of("message", "Successfully unfollowed user"));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowResponseDto>> getFollowers(@PathVariable Long userId){
        return ResponseEntity.ok(service.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowResponseDto>> getFollowing(@PathVariable Long userId){
        return ResponseEntity.ok(service.getFollowing(userId));
    }
}
