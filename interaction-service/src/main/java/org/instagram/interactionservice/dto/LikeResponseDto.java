package org.instagram.interactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponseDto {
    
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;
    
    public LikeResponseDto(Long userId, Long postId, LocalDateTime createdAt) {
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
    }
}
