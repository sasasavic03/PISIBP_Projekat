package org.instagram.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostCountResponse {
    @JsonProperty("count")
    private Long count;

    public PostCountResponse() {
    }

    public PostCountResponse(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

