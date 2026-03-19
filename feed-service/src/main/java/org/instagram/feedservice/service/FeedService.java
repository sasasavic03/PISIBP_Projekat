package org.instagram.feedservice.service;

import org.instagram.feedservice.dto.FeedPostDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeedService {

    private final RestTemplate restTemplate;

    @Value("${follow.service.url:http://follow-service:8080}")
    private String followServiceUrl;

    @Value("${post.service.url:http://post-service:8080}")
    private String postServiceUrl;

    @Value("${user.service.url:http://user-service:8080}")
    private String userServiceUrl;

    public FeedService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "userFeed", key = "#userId + '-' + #page + '-' + #size")
    public Map<String, Object> getUserFeedPaginated(Long userId, int page, int size) {
        try {
            // Fetch all posts first
            List<FeedPostDTO> allPosts = fetchAllUserFeedPosts(userId);

            // Sort by creation date descending (newest first)
            List<FeedPostDTO> sortedPosts = allPosts.stream()
                    .sorted(Comparator.comparing(FeedPostDTO::getCreatedAt).reversed())
                    .collect(Collectors.toList());

            // Calculate pagination
            int totalItems = sortedPosts.size();
            int totalPages = (int) Math.ceil((double) totalItems / size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalItems);

            // Get page content
            List<FeedPostDTO> pageContent = sortedPosts.subList(startIndex, endIndex);

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("content", pageContent);
            response.put("currentPage", page);
            response.put("totalItems", totalItems);
            response.put("totalPages", totalPages);
            response.put("hasNext", page < totalPages - 1);
            response.put("hasPrevious", page > 0);
            response.put("pageSize", size);

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user feed: " + e.getMessage(), e);
        }
    }

    @Cacheable(value = "userFeedFull", key = "#userId")
    public List<FeedPostDTO> getUserFeed(Long userId) {
        try {
            List<FeedPostDTO> allPosts = fetchAllUserFeedPosts(userId);

            // Sort by creation date descending (newest first)
            return allPosts.stream()
                    .sorted(Comparator.comparing(FeedPostDTO::getCreatedAt).reversed())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user feed: " + e.getMessage(), e);
        }
    }

    @Cacheable(value = "profileFeed", key = "#userId + '-' + #page + '-' + #size")
    public Map<String, Object> getUserProfileFeedPaginated(Long userId, int page, int size) {
        try {
            // Fetch all user's posts
            List<FeedPostDTO> allPosts = fetchUserProfilePosts(userId);

            // Sort by creation date descending (newest first)
            List<FeedPostDTO> sortedPosts = allPosts.stream()
                    .sorted(Comparator.comparing(FeedPostDTO::getCreatedAt).reversed())
                    .collect(Collectors.toList());

            // Calculate pagination
            int totalItems = sortedPosts.size();
            int totalPages = (int) Math.ceil((double) totalItems / size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalItems);

            // Get page content
            List<FeedPostDTO> pageContent = sortedPosts.subList(startIndex, endIndex);

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("content", pageContent);
            response.put("currentPage", page);
            response.put("totalItems", totalItems);
            response.put("totalPages", totalPages);
            response.put("hasNext", page < totalPages - 1);
            response.put("hasPrevious", page > 0);
            response.put("pageSize", size);

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user profile feed: " + e.getMessage(), e);
        }
    }

    public List<FeedPostDTO> getUserProfileFeed(Long userId) {
        try {
            List<FeedPostDTO> allPosts = fetchUserProfilePosts(userId);

            // Sort by creation date descending (newest first)
            return allPosts.stream()
                    .sorted(Comparator.comparing(FeedPostDTO::getCreatedAt).reversed())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user profile feed: " + e.getMessage(), e);
        }
    }

    private List<FeedPostDTO> fetchAllUserFeedPosts(Long userId) {
        // Fetch following list
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

        // Fetch posts from followed users
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
    }

    private List<FeedPostDTO> fetchUserProfilePosts(Long userId) {
        // Fetch posts for a specific user
        String postsUrl = postServiceUrl + "/api/posts/feed";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userIds", List.of(userId));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody);

        ResponseEntity<List<FeedPostDTO>> postsResponse = restTemplate.exchange(
                postsUrl,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<FeedPostDTO>>() {}
        );

        List<FeedPostDTO> posts = postsResponse.getBody();
        return posts != null ? posts : new ArrayList<>();
    }

    public Map<String, Object> getUserFeedPaginatedByUsername(String username, int page, int size) {
        Long userId = getUserIdByUsername(username);
        return getUserFeedPaginated(userId, page, size);
    }

    public List<FeedPostDTO> getUserFeedByUsername(String username) {
        Long userId = getUserIdByUsername(username);
        return getUserFeed(userId);
    }


    private Long getUserIdByUsername(String username) {
        try {
            String url = userServiceUrl + "/user/" + username;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getBody() != null && response.getBody().containsKey("id")) {
                Object id = response.getBody().get("id");
                if (id instanceof Number) {
                    return ((Number) id).longValue();
                }
            }
            throw new RuntimeException("User not found: " + username);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user ID for username: " + username, e);
        }
    }
}
