package org.instagram.feedservice.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class PostServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String POST_SERVICE_URL = "http://post-service:8080/api/posts";
    
    @Value("${post.service.url:http://post-service:8080}")
    private String postServiceUrl;

    public List<PostResponse> getPostsByUserIds(List<Long> userIds) {
        try {
            String url = postServiceUrl + "/posts/feed";
            
            Map<String, Object> request = new HashMap<>();
            request.put("userIds", userIds);
            
            PostsResponse response = restTemplate.postForObject(url, request, PostsResponse.class);
            return response != null && response.getPosts() != null ? 
                response.getPosts() : Arrays.asList();
        } catch (Exception e) {
            System.err.println("Error getting posts from Post Service: " + e.getMessage());
            return Arrays.asList();
        }
    }

    public static class PostResponse {
        private Long id;
        private Long userId;
        private String description;
        private Integer mediaCount;
        private Integer likesCount;
        private Integer commentsCount;
        private String createdAt;
        private String updatedAt;
        private List<MediaResponse> mediaList;
        private UserResponse user;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Integer getMediaCount() { return mediaCount; }
        public void setMediaCount(Integer mediaCount) { this.mediaCount = mediaCount; }

        public Integer getLikesCount() { return likesCount; }
        public void setLikesCount(Integer likesCount) { this.likesCount = likesCount; }

        public Integer getCommentsCount() { return commentsCount; }
        public void setCommentsCount(Integer commentsCount) { this.commentsCount = commentsCount; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

        public List<MediaResponse> getMediaList() { return mediaList; }
        public void setMediaList(List<MediaResponse> mediaList) { this.mediaList = mediaList; }

        public UserResponse getUser() { return user; }
        public void setUser(UserResponse user) { this.user = user; }
    }

    public static class MediaResponse {
        private Long id;
        private String mediaUrl;
        private String mediaType;
        private Integer orderIndex;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getMediaUrl() { return mediaUrl; }
        public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

        public String getMediaType() { return mediaType; }
        public void setMediaType(String mediaType) { this.mediaType = mediaType; }

        public Integer getOrderIndex() { return orderIndex; }
        public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    }

    public static class UserResponse {
        private Long id;
        private String username;
        private String email;
        private String bio;
        private String profilePic;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }

        public String getProfilePic() { return profilePic; }
        public void setProfilePic(String profilePic) { this.profilePic = profilePic; }
    }

    public static class PostsResponse {
        private List<PostResponse> posts;

        public List<PostResponse> getPosts() { return posts; }
        public void setPosts(List<PostResponse> posts) { this.posts = posts; }
    }
}
