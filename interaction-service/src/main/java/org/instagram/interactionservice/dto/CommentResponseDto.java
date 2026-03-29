package org.instagram.interactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    
    private Long id;
    private Long userId;
    private Long postId;
    private String content;
    private Boolean isEdited;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // User information for frontend
    private String username;
    private String avatar;
    
    // Constructor without username/avatar for backward compatibility
    public CommentResponseDto(Long id, Long userId, Long postId, String content, 
                              Boolean isEdited, Boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.isEdited = isEdited;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
