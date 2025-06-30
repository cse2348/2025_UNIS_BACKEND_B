package com.example.backend_B.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String author; // 작성자명 받을 필드
    private int views = 0 ; //db에 저장되는값은 null없이 0부터 시작하게...


    public void patch(Article article) {
        if (article.title != null) {
            this.title = article.title;
        }
        if (article.content != null) {
            this.content = article.content;
        }
        if (article.author != null) {
            this.author= article.author;
        }
    }

    public void increaseViews() {
        this.views+= 1;
    }
}