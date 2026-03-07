package org.instagram.follow.dto;

public class FollowRequestDto {
    private Long followerId;
    private Long followingId;
    private boolean isPrivate;

    public Long getFollowerId() {return followerId;}
    public void setFollowerId(Long followerId) {this.followerId = followerId;}

    public Long getFollowingId() {return followingId;}
    public void setFollowingId(Long followingId) {this.followingId = followingId;}

    public boolean isPrivate() {return isPrivate;}
    public void setPrivate(boolean isPrivate) {this.isPrivate = isPrivate;}
}
