package org.instagram.block;


import org.instagram.block.model.Block;
import org.instagram.block.repository.BlockRepository;
import org.instagram.block.service.BlockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlockServiceTest {

    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private BlockService blockService;

    private Block block;

    @BeforeEach
    public void setup() {
        block = new Block();
        block.setId(1L);
        block.setBlockerId(1L);
        block.setBlockedId(1L);
        block.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void block_ShouldSaveBlock_WhenNotAlreadyBlocked() {
        when(blockRepository.existsByBlockerIdAndBlockedId(1L,2L)).thenReturn(false);
        when(blockRepository.save(any(Block.class))).thenReturn(block);

        blockService.block(1L,2L);

        verify(blockRepository,times(1)).save(any(Block.class));
    }

    @Test
    void block_ShouldThrowException_WhenAlreadyBlocked() {
        when(blockRepository.existsByBlockerIdAndBlockedId(1L,2L)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> blockService.block(1L,2L));
        verify(blockRepository,never()).save(any(Block.class));
    }

    @Test
    void ununblock_ShouldDeleteBlock_WhenExists() {
        when(blockRepository.findByBlockerIdAndBlockedId(1L,2L)).thenReturn(Optional.of(block));

        blockService.unblock(1L, 2L);

        verify(blockRepository, times(1)).delete(block);
    }

    @Test
    void unblock_shouldThrowException_WhenNotFound(){
        when(blockRepository.findByBlockerIdAndBlockedId(1L,2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> blockService.unblock(1L,2L));
        verify(blockRepository, never()).delete(any(Block.class));
    }

    @Test
    void isBlocked_ShouldReturnTrue_WhenBlockExists() {
        when(blockRepository.existsByBlockerIdAndBlockedId(1L,2L)).thenReturn(true);

        assertTrue(blockService.isBlocked(1L,2L));
    }

    @Test
    void isBlocked_ShouldReturnFalse_WhenBlockNotExists() {
        when(blockRepository.existsByBlockerIdAndBlockedId(1L,2L)).thenReturn(false);

        assertFalse(blockService.isBlocked(1L,2L));
    }

    @Test
    void getBlockedUsers_ShouldReturnList(){
        when(blockRepository.findByBlockerId(1L)).thenReturn(List.of(block));

        List<Block> result = blockService.getBlockedUsers(1L);

        assertEquals(1,result.size());
        assertEquals(1L,result.get(0).getBlockerId());
    }
}
