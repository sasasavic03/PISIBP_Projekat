package org.instagram.postservice.service;

import jakarta.transaction.Transactional;
import org.instagram.postservice.client.InteractionServiceClient;
import org.instagram.postservice.client.UserServiceClient;
import org.instagram.postservice.dto.PostUpdateDTO;
import org.instagram.postservice.entity.Media;
import org.instagram.postservice.entity.Post;
import org.instagram.postservice.exception.BadRequestException;
import org.instagram.postservice.exception.ResourceNotFoundException;
import org.instagram.postservice.exception.UnauthorizedException;
import org.instagram.postservice.repository.MediaRepository;
import org.instagram.postservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private InteractionServiceClient interactionServiceClient;

    @Autowired
    private UserServiceClient userServiceClient;

    private static final int MAX_MEDIA_COUNT = 20;

    @Transactional
    public Post createPost(Long userId, String description, List<MultipartFile> mediaFiles) {
        if (mediaFiles == null || mediaFiles.isEmpty()) {
            throw new BadRequestException("At least one media is required");
        }
        if (mediaFiles.size() > MAX_MEDIA_COUNT) {
            throw new BadRequestException("Maximum " + MAX_MEDIA_COUNT + " media items allowed");
        }

        Post post = new Post();
        post.setUserId(userId);
        post.setDescription(description);
        post.setMediaCount(mediaFiles.size());
        post.setIsActive(true);

        post = postRepository.save(post);

        java.util.List<String> uploadedFilenames = new java.util.ArrayList<>();
        
        try {
            for (int i = 0; i < mediaFiles.size(); i++) {
                MultipartFile file = mediaFiles.get(i);
                String storedFilename = fileStorageService.storeFile(file);
                uploadedFilenames.add(storedFilename);
                
                Media media = new Media();
                media.setPost(post);
                media.setMediaUrl(storedFilename);
                media.setMediaType(determineMediaType(file.getOriginalFilename()));
                media.setOrderIndex(i);

                mediaRepository.save(media);
            }
        } catch (Exception e) {
            for (String filename : uploadedFilenames) {
                try {
                    fileStorageService.deleteFile(filename);
                } catch (Exception cleanupError) {
                    System.err.println("Warning: Failed to cleanup file during rollback: " + filename + ", Error: " + cleanupError.getMessage());
                }
            }
            throw e;
        }
        return postRepository.findById(post.getId()).orElse(post);
    }


    public Post getPostById(Long postId) {
        return postRepository.findByIdAndIsActiveTrue(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }


    @Transactional
    public List<Post> getUserPosts(Long userId) {
        List<Post> posts = postRepository.findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(userId);
        // Explicitly initialize media collections while in transaction
        for (Post post : posts) {
            if (post.getMediaList() != null) {
                post.getMediaList().forEach(m -> {
                    m.getId(); // Access all fields to force load
                    m.getMediaUrl();
                    m.getMediaType();
                    m.getOrderIndex();
                });
            }
        }
        return posts;
    }
    
    @Transactional
    public List<Post> getPostsByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        List<Post> posts = postRepository.findByUserIdInWithMediaAndIsActiveTrueOrderByCreatedAtDesc(userIds);
        // Explicitly initialize all media fields while in transaction context
        for (Post post : posts) {
            if (post.getMediaList() != null) {
                post.getMediaList().forEach(m -> {
                    m.getId(); // Force load all fields
                    m.getMediaUrl();
                    m.getMediaType();
                    m.getOrderIndex();
                    m.getCreatedAt();
                });
            }
        }
        return posts;
    }

    public Map<String, Object> getPostsByUserIdsPaginated(List<Long> userIds, int page, int size) {
        if (userIds == null || userIds.isEmpty()) {
            Map<String, Object> emptyResponse = new HashMap<>();
            emptyResponse.put("content", List.of());
            emptyResponse.put("page", page);
            emptyResponse.put("totalElements", 0);
            emptyResponse.put("totalPages", 0);
            emptyResponse.put("hasNext", false);
            emptyResponse.put("hasPrevious", false);
            return emptyResponse;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> pageResult = postRepository.findByUserIdInAndIsActiveTrueOrderByCreatedAtDesc(userIds, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageResult.getContent());
        response.put("page", pageResult.getNumber());
        response.put("totalElements", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("hasNext", pageResult.hasNext());
        response.put("hasPrevious", pageResult.hasPrevious());

        return response;
    }

    public void enrichPostsWithCounts(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            return;
        }
        
        try {
            java.util.List<Long> postIds = posts.stream()
                    .map(Post::getId)
                    .collect(java.util.stream.Collectors.toList());
            
            java.util.Map<Long, InteractionServiceClient.PostInteractionCounts> countsMap =
                    interactionServiceClient.getCountsForPosts(postIds);
            
            if (countsMap == null || countsMap.isEmpty()) {
                for (Post post : posts) {
                    enrichPostWithCounts(post);
                }
                return;
            }
            
            for (Post post : posts) {
                InteractionServiceClient.PostInteractionCounts counts = countsMap.get(post.getId());
                if (counts != null) {
                    Long likeCount = counts.getLikeCount();
                    Long commentCount = counts.getCommentCount();
                    post.setLikesCount(likeCount != null ? likeCount.intValue() : 0);
                    post.setCommentsCount(commentCount != null ? commentCount.intValue() : 0);
                } else {
                    post.setLikesCount(0);
                    post.setCommentsCount(0);
                }
            }
        } catch (Exception e) {
            System.err.println("Batch enrichment failed, falling back to individual calls: " + e.getMessage());
            for (Post post : posts) {
                try {
                    enrichPostWithCounts(post);
                } catch (Exception inner) {
                    System.err.println("Error enriching post " + post.getId() + ": " + inner.getMessage());
                }
            }
        }
    }


    @Transactional
    public Post updateDescription(Long postId, Long userId, String newDescription) {
        Post post = getPostById(postId);

        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only update your own posts");
        }

        post.setDescription(newDescription);
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long postId, Long userId, PostUpdateDTO updateDTO) {
        Post post = getPostById(postId);

        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only update your own posts");
        }

        if (updateDTO.getDescription() != null) {
            post.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getIsActive() != null) {
            post.setIsActive(updateDTO.getIsActive());
        }

        if (updateDTO.getMediaIdsToRemove() != null && !updateDTO.getMediaIdsToRemove().isEmpty()) {
            for (Long mediaId : updateDTO.getMediaIdsToRemove()) {
                Media media = mediaRepository.findById(mediaId)
                        .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + mediaId));

                if (!media.getPost().getId().equals(postId)) {
                    throw new BadRequestException("Media does not belong to this post");
                }

                fileStorageService.deleteFile(media.getMediaUrl());
                mediaRepository.delete(media);
            }

            Long newCount = mediaRepository.countByPostId(postId);
            post.setMediaCount(newCount.intValue());

            if (newCount == 0) {
                post.setIsActive(false);
            }
        }

        return postRepository.save(post);
    }



    @Transactional
    public Post removeMedia(Long postId, Long userId, Long mediaId) {
        Post post = getPostById(postId);

        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only modify your own posts");
        }
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));

        if (!media.getPost().getId().equals(postId)) {
            throw new BadRequestException("Media does not belong to this post");
        }

        mediaRepository.delete(media);

        Long newCount = mediaRepository.countByPostId(postId);
        post.setMediaCount(newCount.intValue());

        if (newCount == 0) {
            post.setIsActive(false);
        }
        return postRepository.save(post);
    }

    @Transactional
    public Post removeMediaByIndex(Long postId, Long userId, Integer mediaIndex) {
        Post post = getPostById(postId);

        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only modify your own posts");
        }

        Media media = mediaRepository.findByPostIdAndOrderIndex(postId, mediaIndex)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found at index: " + mediaIndex));

        fileStorageService.deleteFile(media.getMediaUrl());
        mediaRepository.delete(media);

        Long newCount = mediaRepository.countByPostId(postId);
        post.setMediaCount(newCount.intValue());

        if (newCount == 0) {
            post.setIsActive(false);
        }

        return postRepository.save(post);
    }


    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = getPostById(postId);
        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own posts");
        }

        List<Media> mediaList = mediaRepository.findByPostIdOrderByOrderIndexAsc(postId);
        for (Media media : mediaList) {
            fileStorageService.deleteFile(media.getMediaUrl());
        }

        post.setIsActive(false);
        postRepository.save(post);
    }




    private String determineMediaType(String url) {
        String lowerUrl = url.toLowerCase();
        // SUPPORTED MEDIA TYPES
        if (lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".jpeg") ||
                lowerUrl.endsWith(".png") || lowerUrl.endsWith(".gif")) {
            return "IMAGE";
        } else if (lowerUrl.endsWith(".mp4") || lowerUrl.endsWith(".mov") ||
                lowerUrl.endsWith(".avi")) {
            return "VIDEO";
        }
        return "IMAGE";
    }

    public void enrichPostWithCounts(Post post) {
        // Fetch actual counts from Interaction Service
        Long likeCount = interactionServiceClient.getLikeCount(post.getId());
        Long commentCount = interactionServiceClient.getCommentCount(post.getId());
        
        post.setLikesCount(likeCount.intValue());
        post.setCommentsCount(commentCount.intValue());
    }

    public UserServiceClient.UserResponse getUserDetails(Long userId) {
        return userServiceClient.getUserDetails(userId);
    }

    @Transactional
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }
}
