package org.instagram.user_service.client;

import org.instagram.user_service.dto.FollowCountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "follow-service", url = "${follow-service.url:http://follow-service:8080}")
public interface FollowServiceClient {
    
    @GetMapping("/api/follow/{userId}/following")
    List<Long> getFollowingIds(@PathVariable Long userId);
    
    @GetMapping("/api/follow/{userId}/followers")
    List<Long> getFollowerIds(@PathVariable Long userId);
    
    @GetMapping("/api/follow/{userId}/followers/count")
    FollowCountResponse getFollowersCount(@PathVariable Long userId);
    
    @GetMapping("/api/follow/{userId}/following/count")
    FollowCountResponse getFollowingCount(@PathVariable Long userId);
}
