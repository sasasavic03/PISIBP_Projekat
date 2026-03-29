package org.instagram.feedservice.service;

import org.instagram.feedservice.dto.FeedPostDTO;
import org.instagram.feedservice.dto.PageResponse;
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

            // Fetch posts from followed users with pagination
            PageResponse<FeedPostDTO> postsResponse = fetchPostsForUsersPaginated(followingUserIds, page, size);

            // Get the content list
            List<FeedPostDTO> pageContent = postsResponse.getContent();
            if (pageContent == null) {
                pageContent = new ArrayList<>();
            }
            
            // Sort by creation date descending (newest first), null-safe
            List<FeedPostDTO> sortedPosts = pageContent.stream()
                    .sorted(
                        Comparator.comparing(
                            FeedPostDTO::getCreatedAt,
                            Comparator.nullsLast(Comparator.naturalOrder())
                        ).reversed()
                    )
                    .collect(Collectors.toList());

            // Build response with pagination info
            Map<String, Object> response = new HashMap<>();
            response.put("content", sortedPosts);
            response.put("currentPage", postsResponse.getPage());
            response.put("totalItems", postsResponse.getTotalElements());
            response.put("totalPages", postsResponse.getTotalPages());
            response.put("hasNext", postsResponse.getHasNext() != null ? postsResponse.getHasNext() : false);
            response.put("hasPrevious", postsResponse.getHasPrevious() != null ? postsResponse.getHasPrevious() : false);
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
                    .sorted(
                        Comparator.comparing(
                            FeedPostDTO::getCreatedAt,
                            Comparator.nullsLast(Comparator.naturalOrder())
                        ).reversed()
                    )
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
            // Fetch posts from post-service for this user with pagination
            PageResponse<FeedPostDTO> postsResponse = fetchPostsForUsersPaginated(new ArrayList<>(List.of(userId)), page, size);

            // Get the content list
            List<FeedPostDTO> pageContent = postsResponse.getContent();
            if (pageContent == null) {
                pageContent = new ArrayList<>();
            }
            
            // Sort by creation date descending (newest first), null-safe
            List<FeedPostDTO> sortedPosts = pageContent.stream()
                    .sorted(
                        Comparator.comparing(
                            FeedPostDTO::getCreatedAt,
                            Comparator.nullsLast(Comparator.naturalOrder())
                        ).reversed()
                    )
                    .collect(Collectors.toList());

            // Build response with pagination info
            Map<String, Object> response = new HashMap<>();
            response.put("content", sortedPosts);
            response.put("currentPage", postsResponse.getPage());
            response.put("totalItems", postsResponse.getTotalElements());
            response.put("totalPages", postsResponse.getTotalPages());
            response.put("hasNext", postsResponse.getHasNext() != null ? postsResponse.getHasNext() : false);
            response.put("hasPrevious", postsResponse.getHasPrevious() != null ? postsResponse.getHasPrevious() : false);
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
                    .sorted(
                        Comparator.comparing(
                            FeedPostDTO::getCreatedAt,
                            Comparator.nullsLast(Comparator.naturalOrder())
                        ).reversed()
                    )
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

    private Map<String, Object> fetchPostsForUsers(List<Long> userIds, int page, int size) {
        try {
            if (userIds.isEmpty()) {
                Map<String, Object> emptyResponse = new HashMap<>();
                emptyResponse.put("content", new ArrayList<>());
                emptyResponse.put("page", page);
                emptyResponse.put("totalElements", 0);
                emptyResponse.put("totalPages", 0);
                emptyResponse.put("hasNext", false);
                emptyResponse.put("hasPrevious", false);
                return emptyResponse;
            }

            String postsUrl = postServiceUrl + "/api/posts/feed";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userIds", userIds);
            requestBody.put("page", page);
            requestBody.put("size", size);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody);

            ResponseEntity<Map<String, Object>> postsResponse = restTemplate.exchange(
                    postsUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> response = postsResponse.getBody();
            logger.debug("Fetched posts for {} users on page {} with size {}", 
                    userIds.size(), page, size);
            return response != null ? response : new HashMap<>();

        } catch (Exception e) {
            logger.error("Error fetching posts: {}", e.getMessage(), e);
            Map<String, Object> emptyResponse = new HashMap<>();
            emptyResponse.put("content", new ArrayList<>());
            emptyResponse.put("page", page);
            emptyResponse.put("totalElements", 0);
            emptyResponse.put("totalPages", 0);
            emptyResponse.put("hasNext", false);
            emptyResponse.put("hasPrevious", false);
            return emptyResponse;
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
            String url = blockServiceUrl + "/api/users/" + userId + "/blocked-ids";
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

    private FeedPostDTO convertMapToFeedPostDTO(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        
        FeedPostDTO dto = new FeedPostDTO();
        
        // Convert id - handle both Integer and Long
        Object id = map.get("id");
        if (id instanceof Number) {
            dto.setId(((Number) id).longValue());
        }
        
        // Convert userId - handle both Integer and Long
        Object userId = map.get("userId");
        if (userId instanceof Number) {
            dto.setUserId(((Number) userId).longValue());
        }
        
        dto.setDescription((String) map.get("description"));
        
        // Convert mediaCount
        Object mediaCount = map.get("mediaCount");
        if (mediaCount instanceof Number) {
            dto.setMediaCount(((Number) mediaCount).intValue());
        }
        
        // Convert likesCount
        Object likesCount = map.get("likesCount");
        if (likesCount instanceof Number) {
            dto.setLikesCount(((Number) likesCount).intValue());
        }
        
        // Convert commentsCount
        Object commentsCount = map.get("commentsCount");
        if (commentsCount instanceof Number) {
            dto.setCommentsCount(((Number) commentsCount).intValue());
        }
        
        // Convert createdAt
        Object createdAt = map.get("createdAt");
        if (createdAt != null) {
            dto.setCreatedAt((java.time.LocalDateTime) createdAt);
        }
        
        // Convert updatedAt
        Object updatedAt = map.get("updatedAt");
        if (updatedAt != null) {
            dto.setUpdatedAt((java.time.LocalDateTime) updatedAt);
        }
        
        return dto;
    }

    private PageResponse<FeedPostDTO> fetchPostsForUsersPaginated(List<Long> userIds, int page, int size) {
        try {
            if (userIds.isEmpty()) {
                PageResponse<FeedPostDTO> emptyResponse = new PageResponse<>();
                emptyResponse.setContent(new ArrayList<>());
                emptyResponse.setPage(page);
                emptyResponse.setTotalElements(0L);
                emptyResponse.setTotalPages(0);
                emptyResponse.setHasNext(false);
                emptyResponse.setHasPrevious(false);
                return emptyResponse;
            }

            String postsUrl = postServiceUrl + "/api/posts/feed";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userIds", userIds);
            requestBody.put("page", page);
            requestBody.put("size", size);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody);

            ResponseEntity<PageResponse<FeedPostDTO>> postsResponse = restTemplate.exchange(
                    postsUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<PageResponse<FeedPostDTO>>() {}
            );

            PageResponse<FeedPostDTO> response = postsResponse.getBody();
            logger.debug("Fetched posts for {} users on page {} with size {}", 
                    userIds.size(), page, size);
            return response != null ? response : new PageResponse<>();

        } catch (Exception e) {
            logger.error("Error fetching paginated posts: {}", e.getMessage(), e);
            PageResponse<FeedPostDTO> emptyResponse = new PageResponse<>();
            emptyResponse.setContent(new ArrayList<>());
            emptyResponse.setPage(page);
            emptyResponse.setTotalElements(0L);
            emptyResponse.setTotalPages(0);
            emptyResponse.setHasNext(false);
            emptyResponse.setHasPrevious(false);
            return emptyResponse;
        }
    }
}
