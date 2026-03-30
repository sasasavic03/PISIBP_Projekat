package org.instagram.interactionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeWithUserDto {
    
    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    
    private String username;
    
    private String avatar;
    
    @JsonProperty("post_id")
    private Long postId;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}

