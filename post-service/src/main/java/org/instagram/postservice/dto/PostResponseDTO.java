package org.instagram.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private Long userId;
    private String caption;
    private String mediaURL;
    private Integer likesCount ;
    private Integer commentsCount ;
}
