package org.instagram.postservice.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="posts")
@Data
public class Post {

    @Id
    private Long id;
    private Long userId;
    private String caption;
    private String mediaURL;
    private Integer likesCount = 0;
    private Integer commentsCount = 0;



}
