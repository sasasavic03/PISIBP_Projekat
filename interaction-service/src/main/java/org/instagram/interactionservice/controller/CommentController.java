package org.instagram.interactionservice.controller;


import jakarta.validation.Valid;
import org.instagram.interactionservice.client.UserServiceClient;
import org.instagram.interactionservice.dto.CommentRequestDto;
import org.instagram.interactionservice.dto.CommentResponseDto;
import org.instagram.interactionservice.dto.CommentUpdateDto;
import org.instagram.interactionservice.entity.Comment;
import org.instagram.interactionservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "interaction-service (comments)");
        return ResponseEntity.ok(response);
    }



    @PostMapping
    public ResponseEntity<?> addComment(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CommentRequestDto request) {
        Comment comment = commentService.addComment(
                userId,
                request.getPostId(),
                request.getContent()
        );

        CommentResponseDto response = mapToDto(comment);
        
        // Get updated comment count
        Long commentCount = commentService.getCommentCount(request.getPostId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Comment added successfully");
        result.put("comment", response);
        result.put("commentsCount", commentCount);  // Include updated count
        
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Map<String, Object>> getPostComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getPostComments(postId);
        Long count = commentService.getCommentCount(postId);

        List<CommentResponseDto> commentDtos = comments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId);
        response.put("count", count);
        response.put("comments", commentDtos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        CommentResponseDto response = mapToDto(comment);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CommentUpdateDto request) {
        Comment comment = commentService.updateComment(
                commentId,
                userId,
                request.getContent()
        );
        
        CommentResponseDto response = mapToDto(comment);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("X-User-Id") Long userId) {
        commentService.deleteComment(commentId, userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Comment deleted successfully");

        return ResponseEntity.ok(response);
    }


    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Map<String, Long>> getCommentCount(@PathVariable Long postId) {
        Long count = commentService.getCommentCount(postId);

        Map<String, Long> response = new HashMap<>();
        response.put("count", count);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkUserComment(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long postId) {

        List<Comment> comments = commentService.getPostComments(postId);
        boolean hasCommented = comments.stream()
                .anyMatch(c -> c.getUserId().equals(userId) && !c.getIsDeleted());

        Map<String, Boolean> response = new HashMap<>();
        response.put("commented", hasCommented);

        return ResponseEntity.ok(response);
    }


    private CommentResponseDto mapToDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto(
                comment.getId(),
                comment.getUserId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getIsEdited(),
                comment.getIsDeleted(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
        
        // Fetch and populate user information
        try {
            Map<String, Object> userDetails = userServiceClient.getUserDetails(comment.getUserId());
            if (userDetails != null) {
                dto.setUsername((String) userDetails.get("username"));
                dto.setAvatar((String) userDetails.get("profilePictureUrl"));
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch user details for userId " + comment.getUserId() + ": " + e.getMessage());
            // Set default values if user service fails
            dto.setUsername("Unknown User");
            dto.setAvatar(null);
        }
        
        return dto;
    }
}
