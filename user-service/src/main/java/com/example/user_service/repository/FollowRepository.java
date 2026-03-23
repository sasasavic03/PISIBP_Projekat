package org.instagram.user_service.repository;

import org.instagram.user_service.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {

    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    List<Follow> findByFollowerId(UUID followerId);
    List<Follow> findByFollowingId(UUID followingId);

    void deleteByFollowerIdAndFollowingId(UUID followerId, UUID followingId);
}