package org.instagram.postservice.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDTO {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    private String description;

    @NotEmpty(message = "At least one media URL is required")
    private List<String> mediaUrls;
}
