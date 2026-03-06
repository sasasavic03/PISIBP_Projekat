package org.instagram.interactionservice.service;


import org.instagram.interactionservice.entity.Like;
import org.instagram.interactionservice.repository.LikeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    // KASNIJE POSTAVITI PRAVI PORT
    @Value("${post.service.url}")
    private String postServiceUrl;


    @Transactional
    public Like likePost(Long userId, Long postId) {
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new RuntimeException("You already liked this post");
        }


        Like like = new Like();
        like.setUserId(userId);
        like.setPostId(postId);

        Like savedLike = likeRepository.save(like);

        updatePostLikeCount(postId);

        return savedLike;
    }

    @Transactional
    public void unlikePost(Long userId, Long postId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new RuntimeException("You haven't liked this post"));

        likeRepository.delete(like);

        updatePostLikeCount(postId);
    }

    private void updatePostLikeCount(Long postId) {
        try {
            Long likeCount = likeRepository.countByPostId(postId);
            System.out.println("Post " + postId + " now has " + likeCount + " likes");
        }

        catch (Exception e) {
            System.err.println("Failed to update post like count: " + e.getMessage());
        }
    }


    public boolean hasUserLikedPost(Long userId, Long postId) {
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }

    public List<Like> getPostLikes(Long postId) {
        return likeRepository.findByPostIdOrderByCreatedAtDesc(postId);
    }

    public Long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    public List<Like> getUserLikes(Long userId) {
        return likeRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }




}
