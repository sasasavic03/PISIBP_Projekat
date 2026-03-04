package org.instagram.follow.repository;

import org.instagram.follow.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    long countByFollowingIdAndStatus(Long id, FollowStatus status);

    long countByFollowerIdAndStatus(Long id, FollowStatus status);
}
