package org.instagram.postservice.service;
import jakarta.persistence.EntityManager;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private EntityManager entityManager;
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
        post.setOrderIndex(postRepository.countByUserIdAndIsActiveTrue(userId).intValue());
        post = postRepository.save(post);
        saveMediaFiles(post, mediaFiles);
        return postRepository.findById(post.getId()).orElse(post);
    }
    public Post getPostById(Long postId) {
        return postRepository.findByIdAndIsActiveTrue(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }
    @Transactional
    public List<Post> getUserPosts(Long userId) {
        List<Post> posts = postRepository.findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(userId);
        return filterPostsWithMedia(posts);
    }
    @Transactional
    public List<Post> getPostsByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        List<Post> posts = postRepository.findByUserIdInWithMediaAndIsActiveTrueOrderByCreatedAtDesc(userIds);
        return filterPostsWithMedia(posts);
    }
    @Transactional
    public Map<String, Object> getPostsByUserIdsPaginated(List<Long> userIds, int page, int size) {
        if (userIds == null || userIds.isEmpty()) {
            return createEmptyPaginationResponse(page);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> pageResult = postRepository.findByUserIdInAndIsActiveTrueOrderByCreatedAtDesc(userIds, pageable);
        List<Post> activePosts = filterPostsWithMedia(new ArrayList<>(pageResult.getContent()));
        Map<String, Object> response = new HashMap<>();
        response.put("content", activePosts);
        response.put("page", pageResult.getNumber());
        response.put("totalElements", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("hasNext", pageResult.hasNext());
        response.put("hasPrevious", pageResult.hasPrevious());
        return response;
    }
    @Transactional
    public Post updateDescription(Long postId, Long userId, String newDescription) {
        Post post = getPostById(postId);
        validateUserOwnership(post, userId);
        post.setDescription(newDescription);
        return postRepository.save(post);
    }
    @Transactional
    public Post updatePost(Long postId, Long userId, PostUpdateDTO updateDTO) {
        Post post = getPostById(postId);
        validateUserOwnership(post, userId);
        if (updateDTO.getDescription() != null) {
            post.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getIsActive() != null) {
            post.setIsActive(updateDTO.getIsActive());
        }
        if (updateDTO.getMediaIdsToRemove() != null && !updateDTO.getMediaIdsToRemove().isEmpty()) {
            removeMediaItems(post, updateDTO.getMediaIdsToRemove());
        }
        return postRepository.save(post);
    }
    @Transactional
    public Post removeMedia(Long postId, Long userId, Long mediaId) {
        Post post = getPostById(postId);
        validateUserOwnership(post, userId);
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));
        validateMediaBelongsToPost(media, postId);
        deleteMediaAndReorder(post, media);
        postRepository.flush();
        entityManager.refresh(post);
        return post;
    }
    @Transactional
    public Post removeMediaByIndex(Long postId, Long userId, Integer mediaIndex) {
        Post post = getPostById(postId);
        validateUserOwnership(post, userId);
        Media media = mediaRepository.findByPostIdAndOrderIndex(postId, mediaIndex)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found at index: " + mediaIndex));
        deleteMediaAndReorder(post, media);
        postRepository.flush();
        entityManager.refresh(post);
        return post;
    }
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = getPostById(postId);
        validateUserOwnership(post, userId);
        List<Media> mediaList = mediaRepository.findByPostIdOrderByOrderIndexAsc(postId);
        mediaList.forEach(media -> fileStorageService.deleteFile(media.getMediaUrl()));
        Integer deletedPostIndex = post.getOrderIndex();
        post.setIsActive(false);
        postRepository.save(post);
        postRepository.flush();
        reorderPostsAfterDeletion(userId, deletedPostIndex);
    }
    public void enrichPostsWithCounts(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            return;
        }
        try {
            List<Long> postIds = posts.stream().map(Post::getId).toList();
            Map<Long, InteractionServiceClient.PostInteractionCounts> countsMap =
                    interactionServiceClient.getCountsForPosts(postIds);
            if (countsMap == null || countsMap.isEmpty()) {
                posts.forEach(this::enrichPostWithCounts);
                return;
            }
            for (Post post : posts) {
                InteractionServiceClient.PostInteractionCounts counts = countsMap.get(post.getId());
                if (counts != null) {
                    post.setLikesCount(counts.getLikeCount() != null ? counts.getLikeCount().intValue() : 0);
                    post.setCommentsCount(counts.getCommentCount() != null ? counts.getCommentCount().intValue() : 0);
                } else {
                    post.setLikesCount(0);
                    post.setCommentsCount(0);
                }
            }
        } catch (Exception e) {
            System.err.println("Batch enrichment failed, falling back to individual calls");
            posts.forEach(this::enrichPostWithCounts);
        }
    }
    public void enrichPostWithCounts(Post post) {
        Long likeCount = interactionServiceClient.getLikeCount(post.getId());
        Long commentCount = interactionServiceClient.getCommentCount(post.getId());
        post.setLikesCount(likeCount != null ? likeCount.intValue() : 0);
        post.setCommentsCount(commentCount != null ? commentCount.intValue() : 0);
    }
    public UserServiceClient.UserResponse getUserDetails(Long userId) {
        return userServiceClient.getUserDetails(userId);
    }
    @Transactional
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }
    private void saveMediaFiles(Post post, List<MultipartFile> mediaFiles) {
        List<String> uploadedFilenames = new ArrayList<>();
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
            uploadedFilenames.forEach(filename -> {
                try {
                    fileStorageService.deleteFile(filename);
                } catch (Exception ex) {
                    System.err.println("Warning: Failed to cleanup file: " + filename);
                }
            });
            throw e;
        }
    }
    private void removeMediaItems(Post post, List<Long> mediaIds) {
        for (Long mediaId : mediaIds) {
            Media media = mediaRepository.findById(mediaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + mediaId));
            validateMediaBelongsToPost(media, post.getId());
            Integer deletedIndex = media.getOrderIndex();
            fileStorageService.deleteFile(media.getMediaUrl());
            mediaRepository.delete(media);
            reorderMediaAfterDeletion(post.getId(), deletedIndex);
        }
        Long newCount = mediaRepository.countByPostId(post.getId());
        post.setMediaCount(newCount.intValue());
        if (newCount == 0) {
            post.setIsActive(false);
        }
    }
    private void deleteMediaAndReorder(Post post, Media media) {
        Integer deletedIndex = media.getOrderIndex();
        fileStorageService.deleteFile(media.getMediaUrl());
        mediaRepository.delete(media);
        reorderMediaAfterDeletion(post.getId(), deletedIndex);
        Long newCount = mediaRepository.countByPostId(post.getId());
        post.setMediaCount(newCount.intValue());
        if (newCount == 0) {
            post.setIsActive(false);
        }
        postRepository.save(post);
    }
    private List<Post> filterPostsWithMedia(List<Post> posts) {
        List<Post> activePosts = new ArrayList<>();
        for (Post post : posts) {
            entityManager.refresh(post);
            if (post.getMediaList() != null && !post.getMediaList().isEmpty()) {
                activePosts.add(post);
            }
        }
        return activePosts;
    }
    private Map<String, Object> createEmptyPaginationResponse(int page) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", List.of());
        response.put("page", page);
        response.put("totalElements", 0);
        response.put("totalPages", 0);
        response.put("hasNext", false);
        response.put("hasPrevious", false);
        return response;
    }
    @Transactional
    protected void reorderMediaAfterDeletion(Long postId, Integer deletedIndex) {
        mediaRepository.decrementOrderIndexAfterDeletion(postId, deletedIndex);
        mediaRepository.flush();
    }
    @Transactional
    protected void reorderPostsAfterDeletion(Long userId, Integer deletedPostIndex) {
        List<Post> postsToReorder = postRepository.findByUserIdAndIsActiveTrueOrderByOrderIndexAsc(userId);
        List<Post> itemsToUpdate = new ArrayList<>();
        for (Post post : postsToReorder) {
            if (post.getOrderIndex() != null && post.getOrderIndex() > deletedPostIndex) {
                post.setOrderIndex(post.getOrderIndex() - 1);
                itemsToUpdate.add(post);
            }
        }
        if (!itemsToUpdate.isEmpty()) {
            postRepository.saveAll(itemsToUpdate);
            postRepository.flush();
        }
    }
    private String determineMediaType(String filename) {
        if (filename == null) {
            return "IMAGE";
        }
        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg") ||
                lowerFilename.endsWith(".png") || lowerFilename.endsWith(".gif")) {
            return "IMAGE";
        } else if (lowerFilename.endsWith(".mp4") || lowerFilename.endsWith(".mov") ||
                lowerFilename.endsWith(".avi")) {
            return "VIDEO";
        }
        return "IMAGE";
    }
    private void validateUserOwnership(Post post, Long userId) {
        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only modify your own posts");
        }
    }
    private void validateMediaBelongsToPost(Media media, Long postId) {
        if (!media.getPost().getId().equals(postId)) {
            throw new BadRequestException("Media does not belong to this post");
        }
    }
}
