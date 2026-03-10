package org.instagram.feedservice.controller;

import org.instagram.feedservice.dto.FeedPostDTO;
import org.instagram.feedservice.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feed")
@CrossOrigin(origins = "*")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "feed-service");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserFeed(@PathVariable Long userId) {
        try {
            List<FeedPostDTO> feed = feedService.getUserFeed(userId);
            return ResponseEntity.ok(feed);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
