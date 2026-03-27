package org.instagram.interactionservice.service;


import org.instagram.interactionservice.entity.Like;
import org.instagram.interactionservice.exception.BadRequestException;
import org.instagram.interactionservice.exception.ResourceNotFoundException;
import org.instagram.interactionservice.repository.LikeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;


    @Transactional
    public Like likePost(Long userId, Long postId) {
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new BadRequestException("You already liked this post");
        }

        Like like = new Like();
        like.setUserId(userId);
        like.setPostId(postId);

        return likeRepository.save(like);
    }

    @Transactional
    public void unlikePost(Long userId, Long postId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("You haven't liked this post"));

        likeRepository.delete(like);
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
