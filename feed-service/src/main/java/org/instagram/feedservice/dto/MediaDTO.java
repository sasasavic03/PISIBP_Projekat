package org.instagram.feedservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    private Long id;
    private String mediaUrl;
    private String mediaType;
    private Integer orderIndex;
}
