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
    public ResponseEntity<Map<String,String>> block(
            @PathVariable Long userId,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestBody BlockRequestDto request){
        try {
            blockService.block(currentUserId,request.getBlockedId());
            return ResponseEntity.ok(Map.of("message","User blocked successfully"));
        } catch (RuntimeException e){
            if(e.getMessage().equals("Already Blocked")){
                return ResponseEntity.ok(Map.of("message","User already blocked"));
            }
            return ResponseEntity.badRequest().body(Map.of("Error",e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/block")
    public ResponseEntity<Map<String,String>> unblock(
            @PathVariable Long userId,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestBody BlockRequestDto request){
        try {
            blockService.unblock(currentUserId,request.getBlockedId());
            return ResponseEntity.ok(Map.of("message", "User unblocked successfully"));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("Error",e.getMessage()));
        }
    }

    @GetMapping("/{userId}/blocked")
    public ResponseEntity<List<BlockResponseDto>> getBlockedUsers(
            @RequestHeader("X-User-Id") Long currentUserId){
        return ResponseEntity.ok(blockService.getBlockedUsers(currentUserId));
    }

    @GetMapping("/{userId}/blocked-ids")
    public ResponseEntity<List<Long>> getBlockedUserIds(@PathVariable Long userId){
        return ResponseEntity.ok(blockService.getBlockedUserIds(userId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isBlocked(@RequestParam Long blockerId,
                                             @RequestParam Long blockedId){
        return ResponseEntity.ok(blockService.isBlocked(blockerId, blockedId));
    }

}