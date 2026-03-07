package org.instagram.interactionservice.controller;


import org.instagram.interactionservice.entity.Like;
import org.instagram.interactionservice.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> likePost(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long postId = Long.valueOf(request.get("postId").toString());

            Like like = likeService.likePost(userId, postId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Post liked successfully");
            response.put("like", like);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
        }
    }


    @DeleteMapping
    public ResponseEntity<?> unlikePost(
            @RequestParam Long userId,
            @RequestParam Long postId) {
        try {
            likeService.unlikePost(userId, postId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Post unliked successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Map<String, Object>> getPostLikes(@PathVariable Long postId) {
        List<Like> likes = likeService.getPostLikes(postId);
        Long count = likeService.getLikeCount(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId);
        response.put("count", count);
        response.put("likes", likes);

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
            @RequestParam Long userId,
            @RequestParam Long postId) {

        boolean liked = likeService.hasUserLikedPost(userId, postId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("liked", liked);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Like>> getUserLikes(@PathVariable Long userId) {
        List<Like> likes = likeService.getUserLikes(userId);
        return ResponseEntity.ok(likes);
    }


    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }



}
