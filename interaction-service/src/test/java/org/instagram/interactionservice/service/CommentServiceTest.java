package org.instagram.interactionservice.service;

import org.instagram.interactionservice.entity.Comment;
import org.instagram.interactionservice.exception.BadRequestException;
import org.instagram.interactionservice.exception.ResourceNotFoundException;
import org.instagram.interactionservice.exception.UnauthorizedException;
import org.instagram.interactionservice.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService Unit Tests")
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

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

    // ==================== ADD COMMENT TESTS ====================

    @Test
    @DisplayName("Should throw BadRequestException when content is null")
    void addComment_whenContentNull_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> commentService.addComment(1L, 2L, null));
        verifyNoInteractions(commentRepository);
    }

    @Test
    @DisplayName("Should throw BadRequestException when content is blank")
    void addComment_whenContentBlank_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> commentService.addComment(1L, 2L, "   "));
        verifyNoInteractions(commentRepository);
    }

    @Test
    @DisplayName("Should throw BadRequestException when content exceeds 2200 characters")
    void addComment_whenContentTooLong_throwsBadRequest() {
        String tooLong = "a".repeat(2201);
        assertThrows(BadRequestException.class, () -> commentService.addComment(1L, 2L, tooLong));
        verifyNoInteractions(commentRepository);
    }

    @Test
    @DisplayName("Should save comment successfully with trimmed content")
    void addComment_whenValid_savesTrimmedAndUpdatesCount() {
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> {
            Comment c = inv.getArgument(0);
            c.setId(10L);
            return c;
        });
        when(commentRepository.countByPostIdAndIsDeletedFalse(2L)).thenReturn(1L);

        Comment saved = commentService.addComment(1L, 2L, "  hello world  ");

        assertNotNull(saved);
        assertEquals(10L, saved.getId());
        assertEquals(1L, saved.getUserId());
        assertEquals(2L, saved.getPostId());
        assertEquals("hello world", saved.getContent());
        assertFalse(saved.getIsEdited());
        assertFalse(saved.getIsDeleted());

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(captor.capture());
        assertEquals("hello world", captor.getValue().getContent());
    }

    // ==================== GET COMMENT BY ID TESTS ====================

    @Test
    @DisplayName("Should return comment when found")
    void getCommentById_whenFound_returnsComment() {
        when(commentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testComment));

        Comment found = commentService.getCommentById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Test comment", found.getContent());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when comment not found")
    void getCommentById_whenMissing_throwsResourceNotFound() {
        when(commentRepository.findByIdAndIsDeletedFalse(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.getCommentById(5L));
    }

    // ==================== GET POST COMMENTS TESTS ====================

    @Test
    @DisplayName("Should return all active comments for a post")
    void getPostComments_returnsActiveComments() {
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setPostId(2L);
        comment1.setContent("Comment 1");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setPostId(2L);
        comment2.setContent("Comment 2");

        when(commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(2L))
                .thenReturn(List.of(comment1, comment2));

        List<Comment> comments = commentService.getPostComments(2L);

        assertEquals(2, comments.size());
        assertEquals("Comment 1", comments.get(0).getContent());
        assertEquals("Comment 2", comments.get(1).getContent());
    }

    @Test
    @DisplayName("Should return empty list when post has no comments")
    void getPostComments_whenNoComments_returnsEmpty() {
        when(commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(2L))
                .thenReturn(List.of());

        List<Comment> comments = commentService.getPostComments(2L);

        assertTrue(comments.isEmpty());
    }

    // ==================== UPDATE COMMENT TESTS ====================

    @Test
    @DisplayName("Should throw UnauthorizedException when user is not comment owner")
    void updateComment_whenNotOwner_throwsUnauthorized() {
        when(commentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testComment));

        assertThrows(UnauthorizedException.class, () -> commentService.updateComment(1L, 999L, "new content"));
        verify(commentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BadRequestException when updating with blank content")
    void updateComment_whenBlankContent_throwsBadRequest() {
        when(commentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testComment));

        assertThrows(BadRequestException.class, () -> commentService.updateComment(1L, 1L, "   "));
        verify(commentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BadRequestException when content exceeds 2200 characters")
    void updateComment_whenContentTooLong_throwsBadRequest() {
        when(commentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testComment));
        String tooLong = "a".repeat(2201);

        assertThrows(BadRequestException.class, () -> commentService.updateComment(1L, 1L, tooLong));
        verify(commentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update comment content and mark as edited")
    void updateComment_whenValid_updatesContentAndMarksEdited() {
        when(commentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        Comment updated = commentService.updateComment(1L, 1L, "  updated content  ");

        assertEquals("updated content", updated.getContent());
        assertTrue(updated.getIsEdited());
        verify(commentRepository).save(eq(testComment));
    }

    // ==================== DELETE COMMENT TESTS ====================

    @Test
    @DisplayName("Should throw UnauthorizedException when user is not comment owner")
    void deleteComment_whenNotOwner_throwsUnauthorized() {
        when(commentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testComment));

        assertThrows(UnauthorizedException.class, () -> commentService.deleteComment(1L, 999L));
        verify(commentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should soft-delete comment by marking deleted and clearing content")
    void deleteComment_whenValid_marksDeletedAndClears() {
        when(commentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));
        when(commentRepository.countByPostIdAndIsDeletedFalse(2L)).thenReturn(0L);

        commentService.deleteComment(1L, 1L);

        assertTrue(testComment.getIsDeleted());
        assertEquals("[Deleted]", testComment.getContent());
        verify(commentRepository).save(eq(testComment));
    }

    // ==================== COMMENT COUNT TESTS ====================

    @Test
    @DisplayName("Should return comment count for a post")
    void getCommentCount_returnsCorrectCount() {
        when(commentRepository.countByPostIdAndIsDeletedFalse(2L)).thenReturn(5L);

        Long count = commentService.getCommentCount(2L);

        assertEquals(5L, count);
        verify(commentRepository).countByPostIdAndIsDeletedFalse(2L);
    }
}
