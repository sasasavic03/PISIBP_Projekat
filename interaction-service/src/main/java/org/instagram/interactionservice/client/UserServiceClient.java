package org.instagram.interactionservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceClient {
    
    @Value("${user-service.url:http://user-service:8080}")
    private String userServiceUrl;
    
    private final RestTemplate restTemplate;
    
    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserDetails(Long userId) {
        try {
            String url = userServiceUrl + "/users/" + userId;
            return (Map<String, Object>) restTemplate.getForObject(url, Map.class);
        } catch (RestClientException e) {
            // Return fallback data if user service is unavailable
            System.err.println("Error getting user details from user-service: " + e.getMessage());
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("id", userId);
            fallback.put("username", "Unknown User");
            fallback.put("profilePictureUrl", null);
            return fallback;
        }
    }
}


