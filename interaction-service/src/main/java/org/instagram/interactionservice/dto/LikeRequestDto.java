package org.instagram.interactionservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeRequestDto {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Post ID is required")
    private Long postId;
}
