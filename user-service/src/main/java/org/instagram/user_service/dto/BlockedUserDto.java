package org.instagram.user_service.dto;

public class BlockedUserDto {

    private Long id;
    private Long blockerId;
    private Long blockedId;
    private String username;
    private String avatar;

    public Long getId() {return id;}
    public Long getBlockerId() { return blockerId; }
    public Long getBlockedId() { return blockedId; }
    public String getUsername() { return username; }
    public String getAvatar() { return avatar; }
    public void setId(Long id) { this.id = id; }
    public void setBlockerId(Long blockerId) { this.blockerId = blockerId; }
    public void setBlockedId(Long blockedId) { this.blockedId = blockedId; }
    public void setUsername(String username) { this.username = username; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
