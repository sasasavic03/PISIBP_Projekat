package org.instagram.apigateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GatewayController {

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "api-gateway");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Instagram API Gateway");
        response.put("version", "1.0.0");
        response.put("status", "ACTIVE");
        
        Map<String, String> routes = new HashMap<>();
        routes.put("auth", "/api/auth/**");
        routes.put("users", "/api/users/**");
        routes.put("posts", "/api/posts/**");
        routes.put("follows", "/api/follows/**");
        routes.put("interactions", "/api/likes/** and /api/comments/**");
        routes.put("blocks", "/api/blocks/**");
        routes.put("feeds", "/api/feeds/**");
        routes.put("notifications", "/api/notifications/**");
        
        response.put("routes", routes);
        return ResponseEntity.ok(response);
    }
}
