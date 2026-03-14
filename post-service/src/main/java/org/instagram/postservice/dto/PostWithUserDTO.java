package org.instagram.postservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.instagram.postservice.client.UserServiceClient;
import org.instagram.postservice.entity.Media;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostWithUserDTO {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private String description;

    @JsonProperty("media_list")
    private List<Media> mediaList;

    @JsonProperty("media_count")
    private Integer mediaCount;

    @JsonProperty("likes_count")
    private Integer likesCount;

    @JsonProperty("comments_count")
    private Integer commentsCount;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private UserServiceClient.UserResponse user;
}
