package org.instagram.postservice.repository;


import org.instagram.postservice.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media,Long> {

    List<Media> findByPostIdOrderByOrderIndexAsc(Long postId);
    Long countByPostId(Long postId);
}
