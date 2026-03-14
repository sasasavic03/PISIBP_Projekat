package org.instagram.postservice.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class InteractionServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String INTERACTION_SERVICE_URL = "http://localhost:8086/api";

    public Long getLikeCount(Long postId) {
        try {
            String url = INTERACTION_SERVICE_URL + "/likes/post/" + postId + "/count";
            CountResponse response = restTemplate.getForObject(url, CountResponse.class);
            return response != null ? response.getCount() : 0L;
        } catch (Exception e) {
            // Log error and return default
            System.err.println("Error getting like count: " + e.getMessage());
            return 0L;
        }
    }

    public Long getCommentCount(Long postId) {
        try {
            String url = INTERACTION_SERVICE_URL + "/comments/post/" + postId + "/count";
            CountResponse response = restTemplate.getForObject(url, CountResponse.class);
            return response != null ? response.getCount() : 0L;
        } catch (Exception e) {
            // Log error and return default
            System.err.println("Error getting comment count: " + e.getMessage());
            return 0L;
        }
    }

    public static class CountResponse {
        private Long count;

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }
}
