package org.instagram.block.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;

    public UserServiceClient(){
        this.restTemplate = new RestTemplate();
    }

    public UserResponse getUserById(Long userId){
        try {
            return restTemplate.getForObject(
                    "http://user-service:8080/users/" + userId,
                    UserResponse.class
            );
        } catch (Exception e) {
            return null;
        }
    }

    public static class UserResponse{
        private Long id;
        private String username;
        private String avatar;

        public Long getId() {return id;}
        public String  getUsername() {return username;}
        public String getAvatar() {return avatar;}
        public void setId(Long id) {this.id = id;}
        public void setUsername(String username) {this.username = username;}
        public void setAvatar(String avatar) {this.avatar = avatar;}
    }
}
