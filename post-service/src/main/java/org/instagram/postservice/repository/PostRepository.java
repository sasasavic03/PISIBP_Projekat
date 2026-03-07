package org.instagram.postservice.repository;

import org.instagram.postservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post ,Long> {
    Optional<Post> findByIdAndIsActiveTrue(Long id);
    List<Post> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId);
    Long countByUserIdAndIsActiveTrue(Long userId);

}
