package org.instagram.interactionservice.service;

import org.instagram.interactionservice.entity.Like;
import org.instagram.interactionservice.exception.BadRequestException;
import org.instagram.interactionservice.exception.ResourceNotFoundException;
import org.instagram.interactionservice.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LikeService Unit Tests")
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeService likeService;

    private Like testLike;

    @BeforeEach
    void setUp() {
        testLike = new Like();
        testLike.setId(1L);
        testLike.setUserId(1L);
        testLike.setPostId(2L);
        testLike.setCreatedAt(LocalDateTime.now());
    }

    // ==================== LIKE POST TESTS ====================

    @Test
    @DisplayName("Should throw BadRequestException when user already liked post")
    void likePost_whenAlreadyLiked_throwsBadRequest() {
        when(likeRepository.existsByUserIdAndPostId(1L, 2L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> likeService.likePost(1L, 2L));
        verify(likeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should save like successfully when valid")
    void likePost_whenValid_savesAndUpdatesCount() {
        when(likeRepository.existsByUserIdAndPostId(1L, 2L)).thenReturn(false);
        when(likeRepository.save(any(Like.class))).thenAnswer(inv -> {
            Like l = inv.getArgument(0);
            l.setId(100L);
            return l;
        });
        when(likeRepository.countByPostId(2L)).thenReturn(1L);

        Like saved = likeService.likePost(1L, 2L);

        assertNotNull(saved);
        assertEquals(100L, saved.getId());
        assertEquals(1L, saved.getUserId());
        assertEquals(2L, saved.getPostId());
        verify(likeRepository).save(any(Like.class));
    }

    // ==================== UNLIKE POST TESTS ====================

    @Test
    @DisplayName("Should throw ResourceNotFoundException when like doesn't exist")
    void unlikePost_whenMissingLike_throwsNotFound() {
        when(likeRepository.findByUserIdAndPostId(1L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> likeService.unlikePost(1L, 2L));
        verify(likeRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should delete like successfully when it exists")
    void unlikePost_whenExisting_deletesAndUpdatesCount() {
        when(likeRepository.findByUserIdAndPostId(1L, 2L)).thenReturn(Optional.of(testLike));
        when(likeRepository.countByPostId(2L)).thenReturn(0L);

        likeService.unlikePost(1L, 2L);

        verify(likeRepository).delete(testLike);
        verify(likeRepository).countByPostId(2L);
    }

    // ==================== CHECK LIKE TESTS ====================

    @Test
    @DisplayName("Should return true when user liked post")
    void hasUserLikedPost_whenLiked_returnsTrue() {
        when(likeRepository.existsByUserIdAndPostId(1L, 2L)).thenReturn(true);

        boolean liked = likeService.hasUserLikedPost(1L, 2L);

        assertTrue(liked);
        verify(likeRepository).existsByUserIdAndPostId(1L, 2L);
    }

    @Test
    @DisplayName("Should return false when user did not like post")
    void hasUserLikedPost_whenNotLiked_returnsFalse() {
        when(likeRepository.existsByUserIdAndPostId(1L, 2L)).thenReturn(false);

        boolean liked = likeService.hasUserLikedPost(1L, 2L);

        assertFalse(liked);
        verify(likeRepository).existsByUserIdAndPostId(1L, 2L);
    }

    // ==================== GET POST LIKES TESTS ====================

    @Test
    @DisplayName("Should return list of likes for a post")
    void getPostLikes_returnsListOfLikes() {
        Like like1 = new Like();
        like1.setId(1L);
        like1.setPostId(2L);

        Like like2 = new Like();
        like2.setId(2L);
        like2.setPostId(2L);

        when(likeRepository.findByPostIdOrderByCreatedAtDesc(2L))
                .thenReturn(List.of(like1, like2));

        List<Like> likes = likeService.getPostLikes(2L);

        assertEquals(2, likes.size());
        verify(likeRepository).findByPostIdOrderByCreatedAtDesc(2L);
    }

    @Test
    @DisplayName("Should return empty list when post has no likes")
    void getPostLikes_whenNoLikes_returnsEmpty() {
        when(likeRepository.findByPostIdOrderByCreatedAtDesc(2L)).thenReturn(List.of());

        List<Like> likes = likeService.getPostLikes(2L);

        assertTrue(likes.isEmpty());
    }

    // ==================== GET LIKE COUNT TESTS ====================

    @Test
    @DisplayName("Should return correct like count for post")
    void getLikeCount_returnsCorrectCount() {
        when(likeRepository.countByPostId(2L)).thenReturn(5L);

        Long count = likeService.getLikeCount(2L);

        assertEquals(5L, count);
        verify(likeRepository).countByPostId(2L);
    }

    @Test
    @DisplayName("Should return zero when post has no likes")
    void getLikeCount_whenNoLikes_returnsZero() {
        when(likeRepository.countByPostId(2L)).thenReturn(0L);

        Long count = likeService.getLikeCount(2L);

        assertEquals(0L, count);
    }

    // ==================== GET USER LIKES TESTS ====================

    @Test
    @DisplayName("Should return list of likes by user")
    void getUserLikes_returnsUserLikes() {
        Like like1 = new Like();
        like1.setId(1L);
        like1.setUserId(1L);

        Like like2 = new Like();
        like2.setId(2L);
        like2.setUserId(1L);

        when(likeRepository.findByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(like1, like2));

        List<Like> likes = likeService.getUserLikes(1L);

        assertEquals(2, likes.size());
        verify(likeRepository).findByUserIdOrderByCreatedAtDesc(1L);
    }

    @Test
    @DisplayName("Should return empty list when user has no likes")
    void getUserLikes_whenUserHasNoLikes_returnsEmpty() {
        when(likeRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of());

        List<Like> likes = likeService.getUserLikes(1L);

        assertTrue(likes.isEmpty());
    }
}
