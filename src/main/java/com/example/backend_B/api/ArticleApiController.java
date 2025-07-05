package com.example.backend_B.api;

import com.example.backend_B.dto.ArticleForm;
import com.example.backend_B.entity.Article;
import com.example.backend_B.repository.ArticleRepository;
import com.example.backend_B.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ArticleApiController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    // 🔹 인기 게시글 상위 5개 조회 (Redis 캐시 적용됨)
    @GetMapping("/api/posts/popular")
    public List<Article> getPopularArticles() {
        return articleService.getPopularArticles();
    }

    // GET: 전체 게시글 목록
    @GetMapping("/api/posts")
    public List<Article> index() {
        return articleService.index();
    }

    // GET: 단일 게시글 조회 (Redis 캐시 적용)
    @GetMapping("/api/posts/{id}")
    public ResponseEntity<Article> show(@PathVariable Long id) {
        Article article = articleService.show(id);
        return (article != null) ?
                ResponseEntity.ok(article) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // POST: 게시글 생성
    @PostMapping("/api/posts")
    public ResponseEntity<Article> create(@RequestBody ArticleForm dto) {
        Article created = articleService.create(dto);
        return (created != null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // PATCH: 게시글 수정
    @PatchMapping("/api/posts/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody ArticleForm dto) {
        Article updated = articleService.update(id, dto);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // DELETE: 게시글 삭제
    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id) {
        Article deleted = articleService.delete(id);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 트랜잭션 테스트
    @PostMapping("/api/posts/transaction-test")
    public ResponseEntity<List<Article>> transactionTest(@RequestBody List<ArticleForm> dtos) {
        List<Article> createdList = articleService.createArticles(dtos);
        return (createdList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
