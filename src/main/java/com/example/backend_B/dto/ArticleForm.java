package com.example.backend_B.dto;

import com.example.backend_B.entity.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ArticleForm {
    private Long id;
    private String title; // 제목을 받을 필드
    private String content; // 내용을 받을 필드
    private String author; // 작성자 필드

    public Article toEntity() {
        return new Article(id, title, content, author, null);
    }
}