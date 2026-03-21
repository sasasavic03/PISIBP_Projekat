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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


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

        for (int i = 0; i < mediaFiles.size(); i++) {
            MultipartFile file = mediaFiles.get(i);
            String storedFilename = fileStorageService.storeFile(file);
            
            Media media = new Media();
            media.setPost(post);
            media.setMediaUrl(storedFilename);
            media.setMediaType(determineMediaType(file.getOriginalFilename()));
            media.setOrderIndex(i);

            mediaRepository.save(media);
        }
        return postRepository.findById(post.getId()).orElse(post);
    }


    public Post getPostById(Long postId) {
        return postRepository.findByIdAndIsActiveTrue(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }


    public List<Post> getUserPosts(Long userId) {
        return postRepository.findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(userId);
    }
    
    public List<Post> getPostsByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        return postRepository.findByUserIdInAndIsActiveTrueOrderByCreatedAtDesc(userIds);
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

        // Update description if provided
        if (updateDTO.getDescription() != null) {
            post.setDescription(updateDTO.getDescription());
        }

        // Update active status if provided
        if (updateDTO.getIsActive() != null) {
            post.setIsActive(updateDTO.getIsActive());
        }

        // Remove media if specified
        if (updateDTO.getMediaIdsToRemove() != null && !updateDTO.getMediaIdsToRemove().isEmpty()) {
            for (Long mediaId : updateDTO.getMediaIdsToRemove()) {
                Media media = mediaRepository.findById(mediaId)
                        .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + mediaId));

                if (!media.getPost().getId().equals(postId)) {
                    throw new BadRequestException("Media does not belong to this post");
                }

                // Delete file from storage
                fileStorageService.deleteFile(media.getMediaUrl());
                mediaRepository.delete(media);
            }

            // Update media count
            Long newCount = mediaRepository.countByPostId(postId);
            post.setMediaCount(newCount.intValue());

            // If no media left, deactivate post
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

        // Get media by index
        Media media = mediaRepository.findByPostIdAndOrderIndex(postId, mediaIndex)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found at index: " + mediaIndex));

        // Delete file from storage
        fileStorageService.deleteFile(media.getMediaUrl());
        mediaRepository.delete(media);

        // Update media count
        Long newCount = mediaRepository.countByPostId(postId);
        post.setMediaCount(newCount.intValue());

        // If no media left, deactivate post
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

        // Delete all media files associated with the post
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
}
