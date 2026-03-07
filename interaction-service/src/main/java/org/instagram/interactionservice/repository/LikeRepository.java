package org.instagram.interactionservice.repository;

import org.instagram.interactionservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    List<Like> findByPostIdOrderByCreatedAtDesc(Long postId);

    List<Like> findByUserIdOrderByCreatedAtDesc(Long userId);

    Long countByPostId(Long postId);

}
