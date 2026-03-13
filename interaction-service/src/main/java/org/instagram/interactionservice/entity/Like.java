package org.instagram.interactionservice.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes", 
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","post_id"}),
        indexes = {
            @Index(name = "idx_like_post_id", columnList = "post_id"),
            @Index(name = "idx_like_user_id", columnList = "user_id")
        })
@Data
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_id",nullable = false)
    private Long postId;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }




}
