package org.instagram.notification.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FollowServiceClient {

    private final RestTemplate restTemplate;

    public FollowServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public void acceptFollow(Long followerId, Long followingId) {
        try {
            restTemplate.postForObject(
                    "http://follow-service:8080/api/follow/accept?followerId=" + followerId + "&followingId=" + followingId,
                    null,
                    Void.class
            );
        } catch (Exception e) {
            System.out.println("Failed to accept follow: " + e.getMessage());
        }
    }
}