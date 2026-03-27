package org.instagram.feedservice.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.Arrays;

@Service
public class FollowServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.follow.url:http://follow-service:8080}")
    private String followServiceUrl;

    public List<Long> getFollowingList(Long userId) {
        try {
            String url = followServiceUrl + "/api/follow/following/" + userId;
            FollowingResponse response = restTemplate.getForObject(url, FollowingResponse.class);
            return response != null && response.getFollowingIds() != null ? 
                response.getFollowingIds() : Arrays.asList();
        } catch (Exception e) {
            System.err.println("Error getting following list: " + e.getMessage());
            return Arrays.asList();
        }
    }

    public static class FollowingResponse {
        private List<Long> followingIds;

        public List<Long> getFollowingIds() {
            return followingIds;
        }

        public void setFollowingIds(List<Long> followingIds) {
            this.followingIds = followingIds;
        }
    }
}
