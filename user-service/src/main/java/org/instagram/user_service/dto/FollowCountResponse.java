package org.instagram.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FollowCountResponse {
    @JsonProperty("count")
    private Long count;

    public FollowCountResponse() {
    }

    public FollowCountResponse(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

