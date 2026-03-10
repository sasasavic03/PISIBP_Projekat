package org.instagram.postservice.service;

import jakarta.transaction.Transactional;
import org.instagram.postservice.entity.Media;
import org.instagram.postservice.entity.Post;
import org.instagram.postservice.repository.MediaRepository;
import org.instagram.postservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MediaRepository mediaRepository;

    private static final int MAX_MEDIA_COUNT = 20;

    @Transactional
    public Post createPost(Long userId , String description, List<String> mediaUrls) {
        if (mediaUrls == null || mediaUrls.isEmpty()) {
            throw new RuntimeException("At least one media is required");
        }
        if (mediaUrls.size() > MAX_MEDIA_COUNT) {
            throw new RuntimeException("Maximum " + MAX_MEDIA_COUNT + " media items allowd");
        }

        Post post = new Post();
        post.setUserId(userId);
        post.setDescription(description);
        post.setMediaCount(mediaUrls.size());
        post.setIsActive(true);

        post = postRepository.save(post);

        for (int i = 0; i < mediaUrls.size(); i++) {
            Media media = new Media();
            media.setPost(post);
            media.setMediaUrl(mediaUrls.get(i));
            media.setMediaType(determineMediaType(mediaUrls.get(i)));
            media.setOrderIndex(i);

            mediaRepository.save(media);
        }
        return postRepository.findById(post.getId()).orElse(post);
    }


    public Post getPostById(Long postId) {
        return postRepository.findByIdAndIsActiveTrue(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
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
            throw new RuntimeException("You can only update your own posts");
        }

        post.setDescription(newDescription);
        return postRepository.save(post);
    }



    @Transactional
    public Post removeMedia(Long postId, Long userId, Long mediaId) {
        Post post = getPostById(postId);

        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("You can only modify your own posts");
        }
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        if (!media.getPost().getId().equals(postId)) {
            throw new RuntimeException("Media does not belong to this post");
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
    public void deletePost(Long postId, Long userId) {
        Post post = getPostById(postId);
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("You can only delete your own posts");
        }

        post.setIsActive(false);
        postRepository.save(post);
    }




    private String determineMediaType(String url) {
        String lowerUrl = url.toLowerCase();
        // SUPORTED MEDIA TYPES
        if (lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".jpeg") ||
                lowerUrl.endsWith(".png") || lowerUrl.endsWith(".gif")) {
            return "IMAGE";
        } else if (lowerUrl.endsWith(".mp4") || lowerUrl.endsWith(".mov") ||
                lowerUrl.endsWith(".avi")) {
            return "VIDEO";
        }
        return "IMAGE";
    }

}
