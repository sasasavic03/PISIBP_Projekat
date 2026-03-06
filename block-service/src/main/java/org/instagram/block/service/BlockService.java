package org.instagram.block.service;

import org.instagram.block.model.Block;
import org.instagram.block.repository.BlockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlockService {

    private final BlockRepository blockRepository;

    public BlockService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    public void block(Long blockerId, Long blockedId) {
        if(blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)){
            throw new RuntimeException("Already blocked");
        }
        Block block = new Block();
        block.setBlockerId(blockerId);
        block.setBlockedId(blockedId);
        block.setCreatedAt(LocalDateTime.now());
        blockRepository.save(block);
    }

    public void unblock(Long blockerId, Long blockedId) {
        Block block = blockRepository.findByBlockerIdAndBlockedId(blockerId,blockedId)
                .orElseThrow(() -> new RuntimeException("Block not found"));
        blockRepository.delete(block);
    }

    public boolean isBlocked(Long blockerId, Long blockedId) {
        return blockRepository.existsByBlockerIdAndBlockedId(blockerId,blockedId);
    }

    public List<Block> getBlockedUsers(Long blockerId) {
        return blockRepository.findByBlockerId(blockerId);
    }


}
