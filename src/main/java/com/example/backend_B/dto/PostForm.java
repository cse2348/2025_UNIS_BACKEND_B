package com.example.backend_B.dto;

import com.example.backend_B.entity.Post;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
public class PostForm {
    private Long id;
    private String title; // 제목을 받을 필드
    private String content; // 내용을 받을 필드
    private String author; // 작성자명 받을 필드
    private Integer views; // 기본형 -> Wrapper형으로 변경
    private LocalDateTime createdAt;

    public Post toEntity() {
        int safeViews = (views == null )? 0 : views;
        return new Post(id, title, content, author, safeViews,createdAt);
    }


}