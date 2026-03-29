package org.instagram.user_service.client;

import org.instagram.user_service.dto.PostCountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-service", url = "${post-service.url:http://post-service:8080}")
public interface PostServiceClient {
    
    @GetMapping("/api/posts/user/{userId}/count")
    PostCountResponse getUserPostsCount(@PathVariable Long userId);
}


