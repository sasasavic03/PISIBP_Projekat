package org.instagram.follow.controller;


import org.instagram.follow.dto.FollowRequestDto;
import org.instagram.follow.dto.FollowResponseDto;
import org.instagram.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService service;

    public FollowController(FollowService service){
        this.service= service;
    }

    @PostMapping
    public ResponseEntity<Void> follow(@RequestBody FollowRequestDto request){
        service.follow(request.getFollowerId(),request.getFollowingId(),request.isPrivate());
        return ResponseEntity.ok().build();
    }
    @DeleteMapping
    public ResponseEntity<Void> unfollow(@RequestBody FollowRequestDto request){
        service.unfollow(request.getFollowerId(),request.getFollowingId());
        return ResponseEntity.ok().build();
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
