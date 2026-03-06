package org.instagram.block.controller;


import org.instagram.block.model.Block;
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

    @PostMapping("/{blockedId}")
    public ResponseEntity<Void> block(@PathVariable Long blockedId,
                                      @RequestParam Long blockerId) {
        blockService.block(blockerId, blockedId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{blockedId}")
    public ResponseEntity<Void> unblock(@PathVariable Long blockedId,
                                        @RequestParam Long blockerId) {
        blockService.unblock(blockerId, blockedId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{check}")
    public ResponseEntity<Boolean> isBlocked(@PathVariable Long blockedId,
                                             @RequestParam Long blockerId) {
        return ResponseEntity.ok(blockService.isBlocked(blockerId, blockedId));
    }

    @GetMapping("/{blockerId}")
    public ResponseEntity<List<Block>> getBlockedUsers(@PathVariable Long blockerId) {
        return ResponseEntity.ok(blockService.getBlockedUsers(blockerId));
    }

}
