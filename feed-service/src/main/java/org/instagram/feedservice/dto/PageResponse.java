package org.instagram.feedservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    
    private List<T> content;
    
    private Integer page;
    
    @JsonProperty("total_elements")
    private Long totalElements;
    
    @JsonProperty("total_pages")
    private Integer totalPages;
    
    @JsonProperty("has_next")
    private Boolean hasNext;
    
    @JsonProperty("has_previous")
    private Boolean hasPrevious;
}

