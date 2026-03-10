package org.instagram.feedservice.service;

import org.instagram.feedservice.dto.FeedPostDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedService {

    private final RestTemplate restTemplate;

    @Value("${follow.service.url:http://follow-service:8080}")
    private String followServiceUrl;

    @Value("${post.service.url:http://post-service:8080}")
    private String postServiceUrl;

    public FeedService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<FeedPostDTO> getUserFeed(Long userId) {
        try {
            String followingUrl = followServiceUrl + "/follow/" + userId + "/following";
            ResponseEntity<List<Long>> followingResponse = restTemplate.exchange(
                    followingUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Long>>() {}
            );

            List<Long> followingUserIds = followingResponse.getBody();

            if (followingUserIds == null || followingUserIds.isEmpty()) {
                return new ArrayList<>();
            }

            String postsUrl = postServiceUrl + "/api/posts/feed";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userIds", followingUserIds);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody);

            ResponseEntity<List<FeedPostDTO>> postsResponse = restTemplate.exchange(
                    postsUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<List<FeedPostDTO>>() {}
            );

            List<FeedPostDTO> posts = postsResponse.getBody();

            return posts != null ? posts : new ArrayList<>();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user feed: " + e.getMessage(), e);
        }
    }
}
