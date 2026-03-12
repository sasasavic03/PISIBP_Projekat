package org.instagram.interactionservice.service;


import org.instagram.interactionservice.entity.Comment;
import org.instagram.interactionservice.exception.BadRequestException;
import org.instagram.interactionservice.exception.ResourceNotFoundException;
import org.instagram.interactionservice.exception.UnauthorizedException;
import org.instagram.interactionservice.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Value("${post.service.url}")
    private String postServiceUrl;


    @Transactional
    public Comment addComment(Long userId, Long postId, String content ) {
        if (content == null || content.trim().isEmpty()) {
            throw new BadRequestException("Comment content cannot be empty");
        }

        if (content.length() > 2200) {
            throw new BadRequestException("Comment too long (max 2200 characters)");
        }
        // Create comment
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setPostId(postId);
        comment.setContent(content.trim());
        comment.setIsEdited(false);
        comment.setIsDeleted(false);

        Comment savedComment = commentRepository.save(comment);

        updatePostCommentCount(postId);

        return savedComment;
    }

    public List<Comment> getPostComments(Long postId) {
        return commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(postId);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }



    @Transactional
    public Comment updateComment(Long commentId, Long userId, String newContent) {
        Comment comment = getCommentById(commentId);

        if (!comment.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only edit your own comments");
        }

        if (newContent == null || newContent.trim().isEmpty()) {
            throw new BadRequestException("Comment content cannot be empty");
        }

        if (newContent.length() > 2200) {
            throw new BadRequestException("Comment too long (max 2200 characters)");
        }

        comment.setContent(newContent.trim());
        comment.setIsEdited(true);

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = getCommentById(commentId);
        
        if (!comment.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own comments");
        }
        
        comment.setIsDeleted(true);
        comment.setContent("[Deleted]");
        commentRepository.save(comment);

        updatePostCommentCount(comment.getPostId());
    }

    public Long getCommentCount(Long postId) {
        return commentRepository.countByPostIdAndIsDeletedFalse(postId);
    }


    private void updatePostCommentCount(Long postId) {
        try {
            Long commentCount = commentRepository.countByPostIdAndIsDeletedFalse(postId);

            System.out.println("Post " + postId + " now has " + commentCount + " comments");

        } catch (Exception e) {
            System.err.println("Failed to update post comment count: " + e.getMessage());
        }
    }








}
