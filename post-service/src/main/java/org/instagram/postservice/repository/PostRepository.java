package org.instagram.postservice.repository;

import org.instagram.postservice.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post ,Long> {
    Optional<Post> findByIdAndIsActiveTrue(Long id);
    List<Post> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId);
    Long countByUserIdAndIsActiveTrue(Long userId);
    
    List<Post> findByUserIdInAndIsActiveTrueOrderByCreatedAtDesc(List<Long> userIds);
    Page<Post> findByUserIdInAndIsActiveTrueOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);
    
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.mediaList m WHERE p.userId IN :userIds AND p.isActive = true ORDER BY p.createdAt DESC")
    List<Post> findByUserIdInWithMediaAndIsActiveTrueOrderByCreatedAtDesc(@Param("userIds") List<Long> userIds);
    
    List<Post> findByUserIdAndIsActiveTrueOrderByOrderIndexAsc(Long userId);
}
