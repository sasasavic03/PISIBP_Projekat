package org.instagram.follow.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "follower_id" , nullable = false)
    private Long followerId;

    @Column(name = "following_id", nullable = false)
    private Long followingId;

    @Enumerated(EnumType.STRING)
    private FollowStatus status;

    @Column(name = "created_at")
    private LocalDateTime createAt;

    public Long getId(){
        return id;
    }
    public Long getFollowerid(){
        return followerId;
    }
    public Long getFollowingId(){
        return followingId;
    }

    public FollowStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setFollowerid(Long followerid) {
        this.followerId = followerid;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public void setStatus(FollowStatus status) {
        this.status = status;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
