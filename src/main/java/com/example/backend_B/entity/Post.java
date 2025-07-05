package com.example.backend_B.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Entity
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String author;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private int views;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.views = 0;
    }

    public void incrementViews() {
        this.views++;
    }

    public void patch(Post post) {
        if (post.title != null) this.title = post.title;
        if (post.content != null) this.content = post.content;
        if (post.author != null) this.author = post.author;
    }
}
