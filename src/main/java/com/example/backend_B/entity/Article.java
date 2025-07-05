package com.example.backend_B.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class Article {

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

    public void patch(Article article) {
        if (article.title != null) this.title = article.title;
        if (article.content != null) this.content = article.content;
        if (article.author != null) this.author = article.author;
    }
}
