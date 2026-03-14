package org.instagram.postservice.controller;

import org.instagram.postservice.client.UserServiceClient;
import org.instagram.postservice.dto.PostWithUserDTO;
import org.instagram.postservice.entity.Post;
import org.instagram.postservice.service.FileStorageService;
import org.instagram.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "post-service");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestParam Long userId,
            @RequestParam(required = false) String description,
            @RequestPart List<MultipartFile> mediaFiles) {
        try {
            Post post = postService.createPost(userId, description, mediaFiles);
            postService.enrichPostWithCounts(post);
            
            PostWithUserDTO response = buildPostWithUserDTO(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        try {
            Post post = postService.getPostById(id);
            postService.enrichPostWithCounts(post);
            
            PostWithUserDTO response = buildPostWithUserDTO(post);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserPosts(@PathVariable Long userId) {
        try {
            List<Post> posts = postService.getUserPosts(userId);
            
            // Enrich all posts with counts and user details
            List<PostWithUserDTO> response = posts.stream()
                    .map(post -> {
                        postService.enrichPostWithCounts(post);
                        return buildPostWithUserDTO(post);
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    @PostMapping("/feed")
    public ResponseEntity<?> getPostsByUserIds(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> userIds = (List<Long>) request.get("userIds");
            
            List<Post> posts = postService.getPostsByUserIds(userIds);
            
            // Enrich all posts with counts and user details
            List<PostWithUserDTO> response = posts.stream()
                    .map(post -> {
                        postService.enrichPostWithCounts(post);
                        return buildPostWithUserDTO(post);
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }


    @PatchMapping("/{id}/description")
    public ResponseEntity<?> updateDescription(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String description = (String) request.get("description");

            Post post = postService.updateDescription(id, userId, description);
            return ResponseEntity.ok(post);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }



    @DeleteMapping("/{postId}/media/{mediaId}")
    public ResponseEntity<?> removeMedia(
            @PathVariable Long postId,
            @PathVariable Long mediaId,
            @RequestParam Long userId) {
        try {
            Post post = postService.removeMedia(postId, userId, mediaId);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestParam Long userId) {
        try {
            postService.deletePost(id, userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Post deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/media/{filename}")
    public ResponseEntity<?> getMedia(@PathVariable String filename) {
        try {
            byte[] fileContent = fileStorageService.getFile(filename);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .body(fileContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }

    private PostWithUserDTO buildPostWithUserDTO(Post post) {
        UserServiceClient.UserResponse userDetails = postService.getUserDetails(post.getUserId());
        
        PostWithUserDTO dto = new PostWithUserDTO();
        dto.setId(post.getId());
        dto.setUserId(post.getUserId());
        dto.setDescription(post.getDescription());
        dto.setMediaList(post.getMediaList());
        dto.setMediaCount(post.getMediaCount());
        dto.setLikesCount(post.getLikesCount());
        dto.setCommentsCount(post.getCommentsCount());
        dto.setIsActive(post.getIsActive());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        
        // Add user details
        dto.setUser(userDetails);
        
        return dto;
    }
}
