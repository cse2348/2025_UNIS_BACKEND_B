package com.example.backend_B.dto;

import com.example.backend_B.entity.Post;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class PostForm {
    private Long id;
    private String title;
    private String content;
    private String author;

    public Post toEntity() {
        return new Post(id, title, content, author, null, 0);
    }
}
