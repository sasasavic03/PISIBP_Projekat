package org.instagram.feedservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedPostDTO {
    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    
    private String description;
    
    @JsonProperty("media_list")
    private List<MediaDTO> mediaList;
    
    @JsonProperty("media_count")
    private Integer mediaCount;
    
    @JsonProperty("likes_count")
    private Integer likesCount;
    
    @JsonProperty("comments_count")
    private Integer commentsCount;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    // User information from post-service
    private Map<String, Object> user;
}
