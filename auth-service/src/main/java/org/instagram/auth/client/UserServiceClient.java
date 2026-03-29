package org.instagram.auth.client;

import org.instagram.auth.dto.CreateUserRequest;
import org.instagram.auth.dto.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${services.user.url:http://user-service:8080}")
public interface UserServiceClient {

    @PostMapping("/users")
    UserProfileResponse createUserProfile(@RequestBody CreateUserRequest request);

    @GetMapping("/users/username/{username}")
    UserProfileResponse getUserByUsername(@PathVariable("username") String username);
}
