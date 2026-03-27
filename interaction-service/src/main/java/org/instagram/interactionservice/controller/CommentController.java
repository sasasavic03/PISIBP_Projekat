package org.instagram.interactionservice.controller;


import jakarta.validation.Valid;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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


    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }

    private CommentResponseDto mapToDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUserId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getIsEdited(),
                comment.getIsDeleted(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
