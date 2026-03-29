package org.instagram.block.service;

import org.instagram.block.client.UserServiceClient;
import org.instagram.block.dto.BlockResponseDto;
import org.instagram.block.model.Block;
import org.instagram.block.repository.BlockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserServiceClient userServiceClient;

    public BlockService(BlockRepository blockRepository, UserServiceClient userServiceClient) {
        this.blockRepository = blockRepository;
        this.userServiceClient = userServiceClient;
    }

    public void block(Long blockerId, Long blockedId) {
        if (blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            throw new RuntimeException("Already blocked");
        }
        Block block = new Block();
        block.setBlockerId(blockerId);
        block.setBlockedId(blockedId);
        block.setCreatedAt(LocalDateTime.now());
        blockRepository.save(block);
    }

    public void unblock(Long blockerId, Long blockedId) {
        Block block = blockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId)
                .orElseThrow(() -> new RuntimeException("Block not found"));
        blockRepository.delete(block);
    }

    public boolean isBlocked(Long blockerId, Long blockedId) {
        return blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    public List<BlockResponseDto> getBlockedUsers(Long blockerId){
        return blockRepository.findByBlockerId(blockerId)
                .stream()
                .map(b->{
                    UserServiceClient.UserResponse user = userServiceClient.getUserById(b.getBlockedId());
                    String username = user != null ? user.getUsername() : null;
                    String avatar = user != null ? user.getAvatar() : null;
                    return new BlockResponseDto(
                            b.getId(),
                            b.getBlockerId(),
                            b.getBlockedId(),
                            username,
                            avatar,
                            b.getCreatedAt()
                    );
                })
                .toList();
    }

    public List<Long> getBlockedUserIds(Long blockerId) {
        return blockRepository.findByBlockerId(blockerId)
                .stream()
                .map(Block::getBlockedId)
                .toList();
    }
}