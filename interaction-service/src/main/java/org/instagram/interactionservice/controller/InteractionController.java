package org.instagram.interactionservice.controller;

import org.instagram.interactionservice.service.LikeService;
import org.instagram.interactionservice.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {

    private static final Logger logger = LoggerFactory.getLogger(InteractionController.class);

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/batch")
    public ResponseEntity<?> getInteractionCountsForPosts(@RequestBody Map<String, Object> request) {
        try {
            Object postIdsObj = request.get("postIds");
            
            if (postIdsObj == null) {
                return ResponseEntity.ok(new HashMap<>());
            }
            
            List<Long> postIds = new java.util.ArrayList<>();
            
            @SuppressWarnings("unchecked")
            List<Object> idList = (List<Object>) postIdsObj;
            
            for (Object id : idList) {
                if (id instanceof Number) {
                    postIds.add(((Number) id).longValue());
                }
            }
            
            if (postIds.isEmpty()) {
                return ResponseEntity.ok(new HashMap<>());
            }
            
            Map<Long, Map<String, Object>> result = new HashMap<>();
            
            for (Long postId : postIds) {
                Long likeCount = likeService.getLikeCount(postId);
                Long commentCount = commentService.getCommentCount(postId);
                
                Map<String, Object> counts = new HashMap<>();
                counts.put("postId", postId);
                counts.put("likeCount", likeCount);
                counts.put("commentCount", commentCount);
                
                result.put(postId, counts);
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting interaction counts for posts", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}

