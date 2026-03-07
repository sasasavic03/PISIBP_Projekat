package org.instagram.block.dto;

public class BlockRequestDto {

    private Long blockerId;
    private Long blockedId;

    public Long getBlockerId(){ return blockerId;}
    public void setBlockerId(Long blockerId){ this.blockerId = blockerId;}

    public Long getBlockedId(){ return blockedId;}
    public void setBlockedId(Long blockedId){ this.blockedId = blockedId;}
}

