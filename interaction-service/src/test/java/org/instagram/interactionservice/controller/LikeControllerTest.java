package org.instagram.interactionservice.controller;

import org.instagram.interactionservice.dto.LikeRequestDto;
import org.instagram.interactionservice.entity.Like;
import org.instagram.interactionservice.service.LikeService;
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
@DisplayName("LikeController Unit Tests")
class LikeControllerTest {

    @Mock
    private LikeService likeService;

    @InjectMocks
    private LikeController controller;

    private Like testLike;

    @BeforeEach
    void setUp() {
        testLike = new Like();
        testLike.setId(1L);
        testLike.setUserId(1L);
        testLike.setPostId(2L);
        testLike.setCreatedAt(LocalDateTime.now());
    }

    // ==================== HEALTH CHECK TESTS ====================

    @Test
    @DisplayName("Should return UP status on health check")
    void health_returnsUpStatus() {
        ResponseEntity<Map<String, String>> response = controller.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("interaction-service (likes)", response.getBody().get("service"));
    }

    // ==================== LIKE POST TESTS ====================

    @Test
    @DisplayName("Should like post and return CREATED status with like object")
    void likePost_returnsCreatedMessageAndLike() {
        LikeRequestDto req = new LikeRequestDto();
        req.setPostId(2L);

        when(likeService.likePost(1L, 2L)).thenReturn(testLike);

        ResponseEntity<?> resp = controller.likePost(1L, req);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) resp.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("message"));
        assertTrue(body.containsKey("like"));
        assertEquals("Post liked successfully", body.get("message"));
        verify(likeService).likePost(1L, 2L);
    }

    // ==================== UNLIKE POST TESTS ====================

    @Test
    @DisplayName("Should unlike post and return success message")
    void unlikePost_callsServiceAndReturnsOk() {
        LikeRequestDto req = new LikeRequestDto();
        req.setPostId(2L);

        ResponseEntity<?> resp = controller.unlikePost(1L, req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) resp.getBody();
        assertEquals("Post unliked successfully", body.get("message"));
        verify(likeService).unlikePost(1L, 2L);
    }

    // ==================== GET POST LIKES TESTS ====================

    @Test
    @DisplayName("Should return all likes for a post")
    void getPostLikes_returnsCountAndList() {
        Like like1 = new Like();
        like1.setId(1L);
        like1.setPostId(2L);
        like1.setCreatedAt(LocalDateTime.now());

        Like like2 = new Like();
        like2.setId(2L);
        like2.setPostId(2L);
        like2.setCreatedAt(LocalDateTime.now());

        when(likeService.getPostLikes(2L)).thenReturn(List.of(like1, like2));
        when(likeService.getLikeCount(2L)).thenReturn(2L);

        ResponseEntity<Map<String, Object>> resp = controller.getPostLikes(2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(2L, resp.getBody().get("postId"));
        assertEquals(2L, resp.getBody().get("count"));
        assertEquals(2, ((List<?>) resp.getBody().get("likes")).size());
        verify(likeService).getPostLikes(2L);
        verify(likeService).getLikeCount(2L);
    }

    @Test
    @DisplayName("Should return empty list when post has no likes")
    void getPostLikes_whenEmpty_returnsZeroCount() {
        when(likeService.getPostLikes(2L)).thenReturn(List.of());
        when(likeService.getLikeCount(2L)).thenReturn(0L);

        ResponseEntity<Map<String, Object>> resp = controller.getPostLikes(2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(0L, resp.getBody().get("count"));
        assertTrue(((List<?>) resp.getBody().get("likes")).isEmpty());
    }

    // ==================== GET LIKE COUNT TESTS ====================

    @Test
    @DisplayName("Should return like count for a post")
    void getLikeCount_returnsCount() {
        when(likeService.getLikeCount(2L)).thenReturn(10L);

        ResponseEntity<Map<String, Long>> resp = controller.getLikeCount(2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(10L, resp.getBody().get("count"));
        verify(likeService).getLikeCount(2L);
    }

    @Test
    @DisplayName("Should return zero count when post has no likes")
    void getLikeCount_whenZero_returnsZero() {
        when(likeService.getLikeCount(2L)).thenReturn(0L);

        ResponseEntity<Map<String, Long>> resp = controller.getLikeCount(2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(0L, resp.getBody().get("count"));
    }

    // ==================== CHECK LIKE TESTS ====================

    @Test
    @DisplayName("Should return true when user liked post")
    void checkLike_whenLiked_returnsTrue() {
        when(likeService.hasUserLikedPost(1L, 2L)).thenReturn(true);

        ResponseEntity<Map<String, Boolean>> resp = controller.checkLike(1L, 2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().get("liked"));
        verify(likeService).hasUserLikedPost(1L, 2L);
    }

    @Test
    @DisplayName("Should return false when user did not like post")
    void checkLike_whenNotLiked_returnsFalse() {
        when(likeService.hasUserLikedPost(1L, 2L)).thenReturn(false);

        ResponseEntity<Map<String, Boolean>> resp = controller.checkLike(1L, 2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertFalse(resp.getBody().get("liked"));
        verify(likeService).hasUserLikedPost(1L, 2L);
    }

    // ==================== GET USER LIKES TESTS ====================

    @Test
    @DisplayName("Should return list of likes by user")
    void getUserLikes_returnsList() {
        Like like1 = new Like();
        like1.setId(1L);
        like1.setUserId(1L);
        like1.setCreatedAt(LocalDateTime.now());

        Like like2 = new Like();
        like2.setId(2L);
        like2.setUserId(1L);
        like2.setCreatedAt(LocalDateTime.now());

        when(likeService.getUserLikes(1L)).thenReturn(List.of(like1, like2));

        ResponseEntity<?> resp = controller.getUserLikes(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(2, ((List<?>) resp.getBody()).size());
        verify(likeService).getUserLikes(1L);
    }

    @Test
    @DisplayName("Should return empty list when user has no likes")
    void getUserLikes_whenEmpty_returnsEmpty() {
        when(likeService.getUserLikes(1L)).thenReturn(List.of());

        ResponseEntity<?> resp = controller.getUserLikes(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(((List<?>) resp.getBody()).isEmpty());
    }
}
