package org.instagram.feedservice.service;

import org.instagram.feedservice.dto.FeedPostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger logger = LoggerFactory.getLogger(FeedService.class);

    private final RestTemplate restTemplate;

    @Value("${follow.service.url:http://follow-service:8080}")
    private String followServiceUrl;

    @Value("${post.service.url:http://post-service:8080}")
    private String postServiceUrl;

    @Value("${user.service.url:http://user-service:8080}")
    private String userServiceUrl;

    @Value("${block.service.url:http://block-service:8080}")
    private String blockServiceUrl;

    public FeedService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getUserFeedPaginated(Long userId, int page, int size) {
        try {
            // Get following users
            List<Long> followingUserIds = getFollowingUsers(userId);

            // Fetch all posts from followed users
            List<FeedPostDTO> allPosts = fetchPostsForUsers(followingUserIds);

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
            List<FeedPostDTO> pageContent = startIndex < sortedPosts.size() 
                    ? sortedPosts.subList(startIndex, endIndex) 
                    : new ArrayList<>();

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
            logger.error("Failed to fetch user feed for userId {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user feed: " + e.getMessage(), e);
        }
    }

    public List<FeedPostDTO> getUserFeed(Long userId) {
        try {
            List<Long> followingUserIds = getFollowingUsers(userId);

            if (followingUserIds.isEmpty()) {
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
            if (posts == null) {
                posts = new ArrayList<>();
            }
            List<FeedPostDTO> sortedPosts = posts.stream()
                    .sorted(Comparator.comparing(FeedPostDTO::getCreatedAt).reversed())
                    .collect(Collectors.toList());

            logger.debug("Fetched {} posts for userId {}", sortedPosts.size(), userId);
            return sortedPosts;

        } catch (Exception e) {
            logger.error("Failed to fetch full user feed for userId {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user feed: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getUserProfileFeedPaginated(Long userId, int page, int size) {
        try {
            // Fetch all posts from post-service for this user
            List<FeedPostDTO> allPosts = fetchPostsForUsers(new ArrayList<>(List.of(userId)));

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
            List<FeedPostDTO> pageContent = startIndex < sortedPosts.size() 
                    ? sortedPosts.subList(startIndex, endIndex) 
                    : new ArrayList<>();

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
            logger.error("Failed to fetch user profile feed for userId {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user profile feed: " + e.getMessage(), e);
        }
    }

    public List<FeedPostDTO> getUserProfileFeed(Long userId) {
        try {
            String postsUrl = postServiceUrl + "/api/posts/feed";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userIds", new ArrayList<>(List.of(userId)));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody);

            ResponseEntity<List<FeedPostDTO>> postsResponse = restTemplate.exchange(
                    postsUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<List<FeedPostDTO>>() {}
            );

            List<FeedPostDTO> posts = postsResponse.getBody();
            if (posts == null) {
                posts = new ArrayList<>();
            }
            List<FeedPostDTO> sortedPosts = posts.stream()
                    .sorted(Comparator.comparing(FeedPostDTO::getCreatedAt).reversed())
                    .collect(Collectors.toList());

            logger.debug("Fetched {} profile posts for userId {}", sortedPosts.size(), userId);
            return sortedPosts;

        } catch (Exception e) {
            logger.error("Failed to fetch user profile feed for userId {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user profile feed: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getUserFeedPaginatedByUsername(String username, int page, int size) {
        Long userId = getUserIdByUsername(username);
        return getUserFeedPaginated(userId, page, size);
    }

    public List<FeedPostDTO> getUserFeedByUsername(String username) {
        Long userId = getUserIdByUsername(username);
        return getUserFeed(userId);
    }

    private List<Long> getFollowingUsers(Long userId) {
        try {
            String followingUrl = followServiceUrl + "/api/follow/" + userId + "/following";
            ResponseEntity<List<Map<String, Object>>> followingResponse = restTemplate.exchange(
                    followingUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> followingData = followingResponse.getBody();

            if (followingData == null || followingData.isEmpty()) {
                logger.debug("No following data found for userId {}", userId);
                return new ArrayList<>();
            }

            // Extract followingId from each response
            List<Long> followingUserIds = followingData.stream()
                    .map(item -> {
                        Object followingId = item.get("followingId");
                        if (followingId instanceof Number) {
                            return ((Number) followingId).longValue();
                        }
                        return null;
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());

            // Fetch blocked users and filter them out
            List<Long> blockedIds = fetchBlockedUserIds(userId);
            followingUserIds = followingUserIds.stream()
                    .filter(id -> !blockedIds.contains(id))
                    .collect(Collectors.toList());

            logger.debug("User {} is following {} users (after blocking filter)", userId, followingUserIds.size());
            return followingUserIds;

        } catch (Exception e) {
            logger.error("Error getting following list for user {}: {}", userId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private List<FeedPostDTO> fetchPostsForUsers(List<Long> userIds) {
        try {
            if (userIds.isEmpty()) {
                return new ArrayList<>();
            }

            String postsUrl = postServiceUrl + "/api/posts/feed";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userIds", userIds);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody);

            ResponseEntity<List<FeedPostDTO>> postsResponse = restTemplate.exchange(
                    postsUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<List<FeedPostDTO>>() {}
            );

            List<FeedPostDTO> posts = postsResponse.getBody();
            logger.debug("Fetched {} posts for {} users", 
                    posts != null ? posts.size() : 0, userIds.size());
            return posts != null ? posts : new ArrayList<>();

        } catch (Exception e) {
            logger.error("Error fetching posts: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }


    private List<Long> fetchBlockedUserIds(Long userId) {
        try {
            String url = blockServiceUrl + "/api/blocks/" + userId + "/blocked-ids";
            ResponseEntity<List<Long>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Long>>() {}
            );
            List<Long> blocked = response.getBody();
            logger.debug("Fetched {} blocked users for userId {}", 
                    blocked != null ? blocked.size() : 0, userId);
            return blocked != null ? blocked : new ArrayList<>();

        } catch (Exception e) {
            logger.warn("Could not fetch blocked users for {}, proceeding without filter: {}", 
                    userId, e.getMessage());
            return new ArrayList<>();
        }
    }

    private Long getUserIdByUsername(String username) {
        try {
            String url = userServiceUrl + "/users/username/" + username;
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
            logger.error("Failed to fetch user ID for username {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user ID for username: " + username, e);
        }
    }
}



