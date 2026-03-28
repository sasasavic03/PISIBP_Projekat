package org.instagram.postservice.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;

@Service
public class InteractionServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.interaction.url:http://interaction-service:8080}")
    private String interactionServiceUrl;

    public Long getLikeCount(Long postId) {
        try {
            String url = interactionServiceUrl + "/likes/post/" + postId + "/count";
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
            String url = interactionServiceUrl + "/comments/post/" + postId + "/count";
            CountResponse response = restTemplate.getForObject(url, CountResponse.class);
            return response != null ? response.getCount() : 0L;
        } catch (Exception e) {
            // Log error and return default
            System.err.println("Error getting comment count: " + e.getMessage());
            return 0L;
        }
    }

    public Map<Long, PostInteractionCounts> getCountsForPosts(List<Long> postIds) {
        try {
            if (postIds == null || postIds.isEmpty()) {
                return new HashMap<>();
            }
            
            String url = interactionServiceUrl + "/api/interactions/posts/batch";
            Map<String, Object> request = new HashMap<>();
            request.put("postIds", postIds);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            
            Map<Long, PostInteractionCounts> result = new HashMap<>();
            
            if (response != null) {
                for (Map.Entry<String, Object> entry : response.entrySet()) {
                    try {
                        Long postId = Long.parseLong(entry.getKey());
                        @SuppressWarnings("unchecked")
                        Map<String, Object> counts = (Map<String, Object>) entry.getValue();
                        
                        Long likeCount = 0L;
                        Long commentCount = 0L;
                        
                        if (counts.get("likeCount") != null) {
                            Object likeCountObj = counts.get("likeCount");
                            if (likeCountObj instanceof Number) {
                                likeCount = ((Number) likeCountObj).longValue();
                            }
                        }
                        
                        if (counts.get("commentCount") != null) {
                            Object commentCountObj = counts.get("commentCount");
                            if (commentCountObj instanceof Number) {
                                commentCount = ((Number) commentCountObj).longValue();
                            }
                        }
                        
                        result.put(postId, new PostInteractionCounts(postId, likeCount, commentCount));
                    } catch (Exception inner) {
                        System.err.println("Error parsing count for post " + entry.getKey() + ": " + inner.getMessage());
                    }
                }
            }
            
            return result;
        } catch (Exception e) {
            // Log error and return empty map - caller will use default values
            System.err.println("Error getting batch counts: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
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

    public static class PostInteractionCounts {
        private Long postId;
        private Long likeCount;
        private Long commentCount;

        public PostInteractionCounts() {}

        public PostInteractionCounts(Long postId, Long likeCount, Long commentCount) {
            this.postId = postId;
            this.likeCount = likeCount;
            this.commentCount = commentCount;
        }

        public Long getPostId() {
            return postId;
        }

        public void setPostId(Long postId) {
            this.postId = postId;
        }

        public Long getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Long likeCount) {
            this.likeCount = likeCount;
        }

        public Long getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Long commentCount) {
            this.commentCount = commentCount;
        }
    }

    public static class BatchCountRequest {
        private List<Long> postIds;

        public List<Long> getPostIds() {
            return postIds;
        }

        public void setPostIds(List<Long> postIds) {
            this.postIds = postIds;
        }
    }

    public static class BatchCountResponse {
        private Map<Long, PostInteractionCounts> counts;

        public Map<Long, PostInteractionCounts> getCounts() {
            return counts;
        }

        public void setCounts(Map<Long, PostInteractionCounts> counts) {
            this.counts = counts;
        }
    }
}
