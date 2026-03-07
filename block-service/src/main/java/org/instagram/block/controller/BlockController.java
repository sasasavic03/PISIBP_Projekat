package org.instagram.block.controller;

import org.instagram.block.dto.BlockRequestDto;
import org.instagram.block.dto.BlockResponseDto;
import org.instagram.block.service.BlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/block")
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping
    public ResponseEntity<Void> block(@RequestBody BlockRequestDto request) {
        blockService.block(request.getBlockerId(), request.getBlockedId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unblock(@RequestBody BlockRequestDto request) {
        blockService.unblock(request.getBlockerId(), request.getBlockedId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isBlocked(@RequestParam Long blockerId,
                                             @RequestParam Long blockedId) {
        return ResponseEntity.ok(blockService.isBlocked(blockerId, blockedId));
    }

    @GetMapping("/{blockerId}")
    public ResponseEntity<List<BlockResponseDto>> getBlockedUsers(@PathVariable Long blockerId) {
        return ResponseEntity.ok(blockService.getBlockedUsers(blockerId));
    }
}