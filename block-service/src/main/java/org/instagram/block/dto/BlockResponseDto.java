package org.instagram.block.dto;

import java.time.LocalDateTime;

public class BlockResponseDto {

    private Long id;
    private Long blockerId;
    private Long blockedId;
    private LocalDateTime createdAt;

    public BlockResponseDto(Long id, Long blockerId, Long blockedId, LocalDateTime createdAt) {
        this.id = id;
        this.blockerId = blockerId;
        this.blockedId = blockedId;
        this.createdAt = createdAt;
    }

    public Long getId() {return id;}
    public Long getBlockerId() {return blockerId;}
    public Long getBlockedId() {return blockedId;}
    public LocalDateTime getCreatedAt() {return createdAt;}
}
