package org.instagram.follow.dto;

import org.instagram.follow.model.FollowStatus;

import java.time.LocalDateTime;

public class FollowResponseDto {
    private Long id;
    private Long followerId;
    private Long followingId;
    private FollowStatus status;
    private LocalDateTime createAt;

    public FollowResponseDto(Long id, Long followerId, Long followingId, FollowStatus status, LocalDateTime createAt) {
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
        this.status = status;
        this.createAt = createAt;
    }

    public Long getId() {return id;}
    public Long getFollowerId() {return followerId;}
    public Long getFollowingId() {return followingId;}
    public FollowStatus getStatus() {return status;}
    public LocalDateTime getCreateAt() {return createAt;}
}
