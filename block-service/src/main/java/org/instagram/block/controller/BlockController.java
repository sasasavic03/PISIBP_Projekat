package org.instagram.block.controller;

import org.instagram.block.dto.BlockRequestDto;
import org.instagram.block.dto.BlockResponseDto;
import org.instagram.block.service.BlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String,String>> health(){
        return ResponseEntity.ok(Map.of(
                "status","UP",
                "service","block-service"
        ));
    }

    @PostMapping("/{userId}/block")
    public ResponseEntity<Map<String, String>> block(@PathVariable Long userId,
                                      @RequestBody BlockRequestDto request){
        blockService.block(userId, request.getBlockedId());
        return ResponseEntity.ok(Map.of("message", "User blocked successfully"));
    }

    @DeleteMapping("/{userId}/block")
    public ResponseEntity<Map<String, String>> unblock(@PathVariable Long userId,
                                        @RequestBody BlockRequestDto request){
        blockService.unblock(userId, request.getBlockedId());
        return ResponseEntity.ok(Map.of("message", "User unblocked successfully"));
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