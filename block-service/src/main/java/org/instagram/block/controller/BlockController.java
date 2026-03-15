package org.instagram.block.controller;

import org.instagram.block.dto.BlockRequestDto;
import org.instagram.block.dto.BlockResponseDto;
import org.instagram.block.service.BlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping("/{userId}/block")
    public ResponseEntity<Void> block(@PathVariable Long userId,
                                      @RequestBody BlockRequestDto request){
        blockService.block(userId, request.getBlockedId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/block")
    public ResponseEntity<Void> unblock(@PathVariable Long userId,
                                        @RequestBody BlockRequestDto request){
        blockService.unblock(userId, request.getBlockedId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/blocked")
    public ResponseEntity<List<BlockResponseDto>> getBlockedUsers(@PathVariable Long userId){
        return ResponseEntity.ok(blockService.getBlockedUsers(userId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isBlocked(@RequestParam Long blockerId,
                                             @RequestParam Long blockedId){
        return ResponseEntity.ok(blockService.isBlocked(blockerId, blockedId));
    }

}