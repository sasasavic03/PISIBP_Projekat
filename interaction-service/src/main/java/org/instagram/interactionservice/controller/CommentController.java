package org.instagram.interactionservice.controller;


import org.instagram.interactionservice.entity.Comment;
import org.instagram.interactionservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "interaction-service (comments)");
        return ResponseEntity.ok(response);
    }


    /**
     * Add a comment to a post
     * POST /api/comments
     * Body: { "userId": 123, "postId": 1, "content": "Great post!", "parentCommentId": null }
     */

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long postId = Long.valueOf(request.get("postId").toString());
            String content = (String) request.get("content");

            Comment comment = commentService.addComment(userId, postId, content );
            return ResponseEntity.status(HttpStatus.CREATED).body(comment);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Map<String, Object>> getPostComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getPostComments(postId);
        Long count = commentService.getCommentCount(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId);
        response.put("count", count);
        response.put("comments", comments);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable Long commentId) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }


    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String content = (String) request.get("content");

            Comment comment = commentService.updateComment(commentId, userId, content);
            return ResponseEntity.ok(comment);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        try {
            commentService.deleteComment(commentId, userId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment deleted successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
        }
    }


    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Map<String, Long>> getCommentCount(@PathVariable Long postId) {
        Long count = commentService.getCommentCount(postId);

        Map<String, Long> response = new HashMap<>();
        response.put("count", count);

        return ResponseEntity.ok(response);
    }


    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }



}
