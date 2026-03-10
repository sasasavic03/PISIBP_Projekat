package org.instagram.feedservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedPostDTO {
    private Long id;
    private Long userId;
    private String description;
    private List<MediaDTO> mediaList;
    private Integer mediaCount;
    private Integer likesCount;
    private Integer commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
