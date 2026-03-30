package org.instagram.follow.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${user.service.url:http://user-service:8080}")
    private String userServiceUrl;

    public UserServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> getUserInfo(Long userId) {
        try {
            String url = userServiceUrl + "/users/" + userId;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            System.err.println("Error fetching user info: " + e.getMessage());
            return null;
        }
    }
}

