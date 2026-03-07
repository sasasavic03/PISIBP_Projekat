package org.instagram.interactionservice.repository;

import org.instagram.interactionservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(Long postId);

    Long countByPostIdAndIsDeletedFalse(Long postId);

    Optional<Comment> findByIdAndIsDeletedFalse(Long id);
}

