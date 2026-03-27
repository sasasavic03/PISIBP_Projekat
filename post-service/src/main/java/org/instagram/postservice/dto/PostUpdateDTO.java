package org.instagram.postservice.dto;

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
public class PostUpdateDTO {

    private String description;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("media_ids_to_remove")
    private List<Long> mediaIdsToRemove;
}
