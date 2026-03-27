package org.instagram.interactionservice.controller;


import jakarta.validation.Valid;
import org.instagram.interactionservice.dto.LikeRequestDto;
import org.instagram.interactionservice.dto.LikeResponseDto;
import org.instagram.interactionservice.entity.Like;
import org.instagram.interactionservice.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "interaction-service (likes)");
        return ResponseEntity.ok(response);
    }



    @PostMapping
    public ResponseEntity<?> likePost(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody LikeRequestDto request) {
        Like like = likeService.likePost(userId, request.getPostId());

        LikeResponseDto response = new LikeResponseDto(
                like.getId(),
                like.getUserId(),
                like.getPostId(),
                like.getCreatedAt()
        );

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Post liked successfully");
        result.put("like", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @DeleteMapping
    public ResponseEntity<?> unlikePost(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody LikeRequestDto request) {
        likeService.unlikePost(userId, request.getPostId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Post unliked successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Map<String, Object>> getPostLikes(@PathVariable Long postId) {
        List<Like> likes = likeService.getPostLikes(postId);
        Long count = likeService.getLikeCount(postId);

        List<LikeResponseDto> likeDtos = likes.stream()
                .map(like -> new LikeResponseDto(
                        like.getId(),
                        like.getUserId(),
                        like.getPostId(),
                        like.getCreatedAt()
                ))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId);
        response.put("count", count);
        response.put("likes", likeDtos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Map<String, Long>> getLikeCount(@PathVariable Long postId) {
        Long count = likeService.getLikeCount(postId);

        Map<String, Long> response = new HashMap<>();
        response.put("count", count);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkLike(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long postId) {

        boolean liked = likeService.hasUserLikedPost(userId, postId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("liked", liked);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LikeResponseDto>> getUserLikes(@PathVariable Long userId) {
        List<Like> likes = likeService.getUserLikes(userId);
        
        List<LikeResponseDto> likeDtos = likes.stream()
                .map(like -> new LikeResponseDto(
                        like.getId(),
                        like.getUserId(),
                        like.getPostId(),
                        like.getCreatedAt()
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(likeDtos);
    }


    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }



}
