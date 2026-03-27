package org.instagram.interactionservice.controller;

import org.instagram.interactionservice.dto.CommentRequestDto;
import org.instagram.interactionservice.dto.CommentUpdateDto;
import org.instagram.interactionservice.entity.Comment;
import org.instagram.interactionservice.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentController Unit Tests")
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController controller;

    private Comment testComment;

    @BeforeEach
    void setUp() {
        testComment = new Comment();
        testComment.setId(1L);
        testComment.setUserId(1L);
        testComment.setPostId(2L);
        testComment.setContent("Test comment");
        testComment.setIsEdited(false);
        testComment.setIsDeleted(false);
        testComment.setCreatedAt(LocalDateTime.now());
        testComment.setUpdatedAt(LocalDateTime.now());
    }

    // ==================== HEALTH CHECK TESTS ====================

    @Test
    @DisplayName("Should return UP status on health check")
    void health_returnsUpStatus() {
        ResponseEntity<Map<String, String>> response = controller.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("interaction-service (comments)", response.getBody().get("service"));
    }

    // ==================== ADD COMMENT TESTS ====================

    @Test
    @DisplayName("Should add comment and return CREATED status")
    void addComment_returnsCreatedAndDto() {
        CommentRequestDto req = new CommentRequestDto();
        req.setPostId(2L);
        req.setContent("Test comment");

        when(commentService.addComment(1L, 2L, "Test comment")).thenReturn(testComment);

        ResponseEntity<?> resp = controller.addComment(1L, req);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        verify(commentService).addComment(1L, 2L, "Test comment");
    }

    // ==================== GET POST COMMENTS TESTS ====================

    @Test
    @DisplayName("Should return all comments for a post")
    void getPostComments_returnsCountAndList() {
        Comment c = new Comment();
        c.setId(1L);
        c.setUserId(1L);
        c.setPostId(2L);
        c.setContent("Comment");
        c.setIsEdited(false);
        c.setIsDeleted(false);
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());

        when(commentService.getPostComments(2L)).thenReturn(List.of(c));
        when(commentService.getCommentCount(2L)).thenReturn(1L);

        ResponseEntity<Map<String, Object>> resp = controller.getPostComments(2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(2L, resp.getBody().get("postId"));
        assertEquals(1L, resp.getBody().get("count"));
        assertTrue(resp.getBody().containsKey("comments"));
        verify(commentService).getPostComments(2L);
        verify(commentService).getCommentCount(2L);
    }

    @Test
    @DisplayName("Should return empty list when post has no comments")
    void getPostComments_whenEmpty_returnsZeroCount() {
        when(commentService.getPostComments(2L)).thenReturn(List.of());
        when(commentService.getCommentCount(2L)).thenReturn(0L);

        ResponseEntity<Map<String, Object>> resp = controller.getPostComments(2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(0L, resp.getBody().get("count"));
        assertTrue(((List<?>) resp.getBody().get("comments")).isEmpty());
    }

    // ==================== GET COMMENT BY ID TESTS ====================

    @Test
    @DisplayName("Should return comment when found by ID")
    void getComment_returnsComment() {
        when(commentService.getCommentById(1L)).thenReturn(testComment);

        ResponseEntity<?> resp = controller.getComment(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        verify(commentService).getCommentById(1L);
    }

    // ==================== UPDATE COMMENT TESTS ====================

    @Test
    @DisplayName("Should update comment and return updated object")
    void updateComment_callsServiceAndReturnsOk() {
        CommentUpdateDto req = new CommentUpdateDto();
        req.setContent("Updated content");

        Comment updated = new Comment();
        updated.setId(1L);
        updated.setUserId(1L);
        updated.setPostId(2L);
        updated.setContent("Updated content");
        updated.setIsEdited(true);
        updated.setIsDeleted(false);
        updated.setCreatedAt(LocalDateTime.now());
        updated.setUpdatedAt(LocalDateTime.now());

        when(commentService.updateComment(1L, 1L, "Updated content")).thenReturn(updated);

        ResponseEntity<?> resp = controller.updateComment(1L, 1L, req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        verify(commentService).updateComment(1L, 1L, "Updated content");
    }

    // ==================== DELETE COMMENT TESTS ====================

    @Test
    @DisplayName("Should delete comment and return success message")
    void deleteComment_callsServiceAndReturnsMessage() {
        ResponseEntity<?> resp = controller.deleteComment(1L, 1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) resp.getBody();
        assertTrue(body.containsKey("message"));
        assertEquals("Comment deleted successfully", body.get("message"));
        verify(commentService).deleteComment(1L, 1L);
    }

    // ==================== COMMENT COUNT TESTS ====================

    @Test
    @DisplayName("Should return comment count for a post")
    void getCommentCount_returnsCount() {
        when(commentService.getCommentCount(2L)).thenReturn(5L);

        ResponseEntity<Map<String, Long>> resp = controller.getCommentCount(2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(5L, resp.getBody().get("count"));
        verify(commentService).getCommentCount(2L);
    }

    @Test
    @DisplayName("Should return zero count when post has no comments")
    void getCommentCount_whenZero_returnsZero() {
        when(commentService.getCommentCount(2L)).thenReturn(0L);

        ResponseEntity<Map<String, Long>> resp = controller.getCommentCount(2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(0L, resp.getBody().get("count"));
    }
}
