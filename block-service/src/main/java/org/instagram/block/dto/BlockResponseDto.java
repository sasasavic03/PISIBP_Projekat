package org.instagram.block.dto;

import java.time.LocalDateTime;

public class BlockResponseDto {

    private Long id;
    private Long blockerId;
    private Long blockedId;
    private String username;
    private String avatar;
    private LocalDateTime createdAt;

    public BlockResponseDto(Long id, Long blockerId, Long blockedId, String username, String avatar, LocalDateTime createdAt){
        this.id = id;
        this.blockerId = blockerId;
        this.blockedId = blockedId;
        this.username = username;
        this.avatar = avatar;
        this.createdAt = createdAt;
    }

    public Long getId() {return id;}
    public Long getBlockerId() {return blockerId;}
    public Long getBlockedId() {return blockedId;}
    public String getUsername() {return username;}
    public String getAvatar() {return avatar;}
    public LocalDateTime getCreatedAt() {return createdAt;}
}
