package org.instagram.postservice.controller;

import org.instagram.postservice.client.UserServiceClient;
import org.instagram.postservice.dto.PostUpdateDTO;
import org.instagram.postservice.dto.PostWithUserDTO;
import org.instagram.postservice.entity.Post;
import org.instagram.postservice.service.FileStorageService;
import org.instagram.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) String description,
            @RequestPart(required = false) List<MultipartFile> files,
            @RequestPart(required = false) List<MultipartFile> mediaFiles) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid user ID"));
            }
            if (description != null && description.length() > 2000) {
                return ResponseEntity.badRequest().body(createErrorResponse("Description cannot exceed 2000 characters"));
            }

            if (files != null && mediaFiles != null) {
                return ResponseEntity.badRequest().body(createErrorResponse("Use either 'files' or 'mediaFiles' parameter, not both"));
            }

            List<MultipartFile> uploadFiles = files != null ? files : mediaFiles;
            
            if (uploadFiles == null) {
                uploadFiles = new ArrayList<>();
            }
            
            Post post = postService.createPost(userId, description, uploadFiles);
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
            
            postService.enrichPostsWithCounts(posts);
            
            List<PostWithUserDTO> response = posts.stream()
                    .map(this::buildPostWithUserDTO)
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
            
            postService.enrichPostsWithCounts(posts);
            
            List<PostWithUserDTO> response = posts.stream()
                    .map(this::buildPostWithUserDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }


    @PatchMapping("/{id}/description")
    public ResponseEntity<?> updateDescription(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, Object> request) {
        try {
            String description = (String) request.get("description");

            Post post = postService.updateDescription(id, userId, description);
            postService.enrichPostWithCounts(post);
            
            PostWithUserDTO response = buildPostWithUserDTO(post);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody PostUpdateDTO updateDTO) {
        try {
            Post post = postService.updatePost(id, userId, updateDTO);
            postService.enrichPostWithCounts(post);
            
            PostWithUserDTO response = buildPostWithUserDTO(post);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }



    @DeleteMapping("/{postId}/media/{mediaId}")
    public ResponseEntity<?> removeMedia(
            @PathVariable Long postId,
            @PathVariable Long mediaId,
            @RequestHeader("X-User-Id") Long userId) {
        try {
            Post post = postService.removeMedia(postId, userId, mediaId);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{postId}/media/index/{mediaIndex}")
    public ResponseEntity<?> removeMediaByIndex(
            @PathVariable Long postId,
            @PathVariable Integer mediaIndex,
            @RequestHeader("X-User-Id") Long userId) {
        try {
            Post post = postService.removeMediaByIndex(postId, userId, mediaIndex);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
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
            // Validate filename to prevent directory traversal attacks
            if (filename.contains("..") || filename.contains("/")) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Invalid filename"));
            }

            if (!fileStorageService.fileExists(filename)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Media file not found"));
            }

            byte[] fileContent = fileStorageService.getFile(filename);
            String contentType = fileStorageService.getContentType(filename);
            long fileSize = fileStorageService.getFileSize(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(fileSize)
                    .header("Content-Disposition", "inline; filename=\"" + filename + "\"")
                    .body(fileContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{postId}/media/{mediaId}")
    public ResponseEntity<?> getMediaByPostAndMediaId(
            @PathVariable Long postId,
            @PathVariable Long mediaId) {
        try {
            Post post = postService.getPostById(postId);
            org.instagram.postservice.entity.Media media = post.getMediaList().stream()
                    .filter(m -> m.getId().equals(mediaId))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Media not found in post"));

            if (!fileStorageService.fileExists(media.getMediaUrl())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Media file not found"));
            }

            byte[] fileContent = fileStorageService.getFile(media.getMediaUrl());
            String contentType = fileStorageService.getContentType(media.getMediaUrl());
            long fileSize = fileStorageService.getFileSize(media.getMediaUrl());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(fileSize)
                    .header("Content-Disposition", "inline; filename=\"" + media.getMediaUrl() + "\"")
                    .body(fileContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }

    private PostWithUserDTO buildPostWithUserDTO(Post post) {
        UserServiceClient.UserResponse userDetails = null;
        
        try {
            userDetails = postService.getUserDetails(post.getUserId());
        } catch (Exception e) {
            userDetails = new UserServiceClient.UserResponse();
            userDetails.setId(post.getUserId());
            userDetails.setUsername("Unknown User");
        }
        
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
