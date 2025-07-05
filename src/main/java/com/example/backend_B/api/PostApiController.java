package com.example.backend_B.api;

import com.example.backend_B.dto.PostForm;
import com.example.backend_B.entity.Post;
import com.example.backend_B.repository.PostRepository;
import com.example.backend_B.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class PostApiController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    // 🔹 인기 게시글 상위 5개 조회 (Redis 캐시 적용됨)
    @GetMapping("/api/posts/popular")
    public List<Post> getPopularPosts() {
        return postService.getPopularPosts();
    }

    // 전체 게시글 목록 조회
    @GetMapping("/api/posts")
    public List<Post> index() {
        return postService.index();
    }

    // 단일 게시글 조회 (Redis 캐시 적용)
    @GetMapping("/api/posts/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id) {
        Post post = postService.show(id);
        return (post != null) ?
                ResponseEntity.ok(post) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 게시글 생성
    @PostMapping("/api/posts")
    public ResponseEntity<Post> create(@RequestBody PostForm dto) {
        Post created = postService.create(dto);
        return (created != null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 게시글 수정
    @PatchMapping("/api/posts/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody PostForm dto) {
        Post updated = postService.update(id, dto);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 게시글 삭제
    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<Post> delete(@PathVariable Long id) {
        Post deleted = postService.delete(id);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 트랜잭션 테스트
    @PostMapping("/api/posts/transaction-test")
    public ResponseEntity<List<Post>> transactionTest(@RequestBody List<PostForm> dtos) {
        List<Post> createdList = postService.createArticles(dtos);
        return (createdList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
