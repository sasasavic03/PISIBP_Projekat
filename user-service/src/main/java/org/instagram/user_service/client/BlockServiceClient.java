package org.instagram.user_service.client;

import org.instagram.user_service.dto.BlockedUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "block-service", url = "${block-service.url:http://block-service:8080}")
public interface BlockServiceClient {
    
    @GetMapping("/api/users/{id}/blocked")
    List<BlockedUserDto> getBlockedUsers(@PathVariable Long id);
}

