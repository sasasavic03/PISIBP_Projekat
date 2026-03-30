package org.instagram.postservice.repository;


import org.instagram.postservice.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media,Long> {

    List<Media> findByPostIdOrderByOrderIndexAsc(Long postId);
    Long countByPostId(Long postId);
    Optional<Media> findByPostIdAndOrderIndex(Long postId, Integer orderIndex);
    
    @Modifying
    @Query("UPDATE Media m SET m.orderIndex = m.orderIndex - 1 WHERE m.post.id = :postId AND m.orderIndex > :deletedIndex")
    void decrementOrderIndexAfterDeletion(@Param("postId") Long postId, @Param("deletedIndex") Integer deletedIndex);
}
