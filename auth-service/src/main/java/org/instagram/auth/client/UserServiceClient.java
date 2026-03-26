package org.instagram.auth.client;

import org.instagram.auth.dto.CreateUserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${user-service.url:http://localhost:8082}")
public interface UserServiceClient {
    
    @PostMapping("/users")
    void createUserProfile(@RequestBody CreateUserRequest request);
}
