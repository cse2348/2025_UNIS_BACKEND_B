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
    private String author; // 작성자명 받을 필드
    private Integer views; // 기본형 -> Wrapper형으로 변경

    public Article toEntity() {
        int safeViews = (views == null )? 0 : views;
        return new Article(id, title, content, author, safeViews);
    }


}