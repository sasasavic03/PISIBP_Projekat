package org.instagram.follow.service;

import jakarta.transaction.Transactional;
import org.instagram.follow.model.Follow;
import org.instagram.follow.model.FollowStatus;
import org.instagram.follow.repository.FollowRepository;

import java.time.LocalDateTime;

public class FollowService {

    private final FollowRepository repository;

    public FollowService(FollowRepository repository){
        this.repository = repository;
    }

    @Transactional
    public void follow(Long followerId, Long followingId,boolean isPrivate){
        if(repository.findByFollowerIdAndFollowingId(followerId,followingId).isPresent())
            throw new RuntimeException("Vec postoji relacija");
        Follow follow = new Follow();
        follow.setFollowerid(followerId);
        follow.setFollowingId(followingId);
        follow.setCreateAt(LocalDateTime.now());
        follow.setStatus(isPrivate ? FollowStatus.PENDING:FollowStatus.ACCEPTED);

        repository.save(follow);
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId){
        Follow follow = repository
                .findByFollowerIdAndFollowingId(followerId,followingId)
                .orElseThrow();

        repository.delete(follow);
    }
}
