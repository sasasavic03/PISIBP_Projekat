package org.instagram.postservice.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

@Entity
@Table(name="posts")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id" , nullable = false)
    private Long userId;

    @Column(length = 2000)
    private String description;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Media> mediaList = new ArrayList<>();

    @Column(name = "media_count")
    private Integer mediaCount;

    @Column(name = "likes_count")
    private Integer likesCount = 0;
    @Column(name = "comments_count")
    private Integer commentsCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;

    @Column(name = "order_index")
    private Integer orderIndex;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }



}
