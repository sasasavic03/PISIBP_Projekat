package org.instagram.postservice.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Service
public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.user.url:http://user-service:8080}")
    private String userServiceUrl;

    public UserResponse getUserDetails(Long userId) {
        try {
            String url = userServiceUrl + "/users/" + userId;
            return restTemplate.getForObject(url, UserResponse.class);
        } catch (Exception e) {
            // Log error and return basic user info
            System.err.println("Error getting user details: " + e.getMessage());
            UserResponse fallback = new UserResponse();
            fallback.setId(userId);
            fallback.setUsername("Unknown User");
            return fallback;
        }
    }

    public static class UserResponse {
        private Long id;
        private String username;
        private String email;
        private String bio;
        
        @com.fasterxml.jackson.annotation.JsonProperty("profilePictureUrl")
        private String profilePictureUrl;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getProfilePictureUrl() {
            return profilePictureUrl;
        }

        public void setProfilePictureUrl(String profilePictureUrl) {
            this.profilePictureUrl = profilePictureUrl;
        }
    }
}
