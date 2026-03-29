package org.instagram.follow.service;

import jakarta.transaction.Transactional;
import org.instagram.follow.client.NotificationClient;
import org.instagram.follow.dto.FollowResponseDto;
import org.instagram.follow.model.Follow;
import org.instagram.follow.model.FollowStatus;
import org.instagram.follow.repository.FollowRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FollowService {

    private final FollowRepository repository;
    private final NotificationClient notificationClient;

    public FollowService(FollowRepository repository, NotificationClient notificationClient) {
        this.repository = repository;
        this.notificationClient = notificationClient;
    }

    @Transactional
    public void follow(Long followerId, Long followingId, boolean isPrivate){
        if(repository.findByFollowerIdAndFollowingId(followerId,followingId).isPresent())
            throw new RuntimeException("Vec postoji relacija");

        Follow follow = new Follow();
        follow.setFollowerid(followerId);
        follow.setFollowingId(followingId);
        follow.setCreateAt(LocalDateTime.now());
        follow.setStatus(isPrivate ? FollowStatus.PENDING:FollowStatus.ACCEPTED);

        repository.save(follow);

        if(isPrivate){
            notificationClient.sendNotification(followingId,followerId, "FOLLOW_REQUEST");
        } else{
            notificationClient.sendNotification(followingId,followerId,"NEW_FOLLOWER");
        }
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId){
        Follow follow = repository
                .findByFollowerIdAndFollowingId(followerId,followingId)
                .orElseThrow();

        repository.delete(follow);
    }

    public List<FollowResponseDto> getFollowers(Long userId){
        return repository.findByFollowingIdAndStatus(userId,FollowStatus.ACCEPTED)
                .stream()
                .map(f-> new FollowResponseDto(
                        f.getId(),
                        f.getFollowerid(),
                        f.getFollowingId(),
                        f.getStatus(),
                        f.getCreateAt()
                ))
                .toList();
    }

    public List<FollowResponseDto> getFollowing(Long userId){
        return repository.findByFollowerIdAndStatus(userId,FollowStatus.ACCEPTED)
                .stream()
                .map(f-> new FollowResponseDto(
                        f.getId(),
                        f.getFollowerid(),
                        f.getFollowingId(),
                        f.getStatus(),
                        f.getCreateAt()
                ))
                .toList();
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return repository.existsByFollowerIdAndFollowingIdAndStatus(followerId, followingId, FollowStatus.ACCEPTED);
    }

    public Long getFollowersCount(Long userId) {
        return repository.countByFollowingIdAndStatus(userId, FollowStatus.ACCEPTED);
    }

    public Long getFollowingCount(Long userId) {
        return repository.countByFollowerIdAndStatus(userId, FollowStatus.ACCEPTED);
    }
}

