package org.instagram.user_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID followerId;
    private UUID followingId;

    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public UUID getFollowingId() {
        return followingId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFollowerId(UUID followerId) {
        this.followerId = followerId;
    }

    public void setFollowingId(UUID followingId) {
        this.followingId = followingId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}