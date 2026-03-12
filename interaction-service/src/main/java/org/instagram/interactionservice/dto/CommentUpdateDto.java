package org.instagram.interactionservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentUpdateDto {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotBlank(message = "Content is required")
    @Size(max = 2200, message = "Comment content cannot exceed 2200 characters")
    private String content;
}
