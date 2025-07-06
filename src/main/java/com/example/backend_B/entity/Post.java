package com.example.backend_B.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "article") // 실제 DB 테이블명과 정확히 일치시켜야 함
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String title;
    private String content;
    private String author; // 작성자명 받을 필드
    private int views = 0; //db에 저장되는값은 null없이 0부터 시작하게...

    @Column(updatable = false) //수정시 변경안되도록
    private LocalDateTime createdAt; //



    @PrePersist //db 저장(insert) 직전에 자동실행. 엔티티 생성시점에 자동으로 값 초기화
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    public void increaseViews() {
        this.views+= 1; //조회수 증가 메서드
    }

    public void patch(Post post) {
        if (post.title != null) {
            this.title = post.title;
        }
        if (post.content != null) {
            this.content = post.content;
        }
        if (post.author != null) {
            this.author= post.author;
        }
    }


}