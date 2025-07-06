package com.example.backend_B.service;

import com.example.backend_B.dto.PostForm;
import com.example.backend_B.entity.Post;
import com.example.backend_B.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> index() {
        return postRepository.findAll();
    }

    public Post show(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post create(PostForm dto) {
        Post post = dto.toEntity();
        if (post.getId() != null) {
            return null;
        }
        return postRepository.save(post);
    }

    public Post update(Long id, PostForm dto) {
        // 1. DTO -> 엔티티 변환하기
        Post post = dto.toEntity();
        log.info("id: {}, article: {}", id, post.toString());
        // 2. 타깃 조회하기
        Post target = postRepository.findById(id).orElse(null);
        // 3. 잘못된 요청 처리하기
        if (target == null || id != post.getId()) {
            // 400, 잘못된 요청 응답!
            log.info("잘못된 요청! id: {}, article: {}", id, post.toString());
            return null;
        }
        // 4. 업데이트 및 정상 응답(200)하기
        target.patch(post);
        Post updated = postRepository.save(target);
        return updated;
    }

    public Post delete(Long id) {
        // 1. 대상 찾기
        Post target = postRepository.findById(id).orElse(null);
        // 2. 잘못된 요청 처리하기
        if (target == null) {
            return null;
        }
        // 3. 대상 삭제하기
        postRepository.delete(target);
        return target;
    }
    @Transactional
    public List<Post> createArticles(List<PostForm> dtos) {
        // 1. dto 묶음을 엔티티 묶음으로 변환하기
        List<Post> postList = dtos.stream()
                .map(dto -> dto.toEntity())
                .collect(Collectors.toList());
        // 2. 엔티티 묶음을 DB에 저장하기
        postList.stream()
                .forEach(article -> postRepository.save(article));
        // 3. 강제 예외 발생시키기
        postRepository.findById(-1L)
                .orElseThrow(() -> new IllegalArgumentException("결제 실패!"));
        // 4. 결과 값 반환하기
        return postList;
    }

    // 인기 게시글 조회 (Redis 캐시 적용)
    @Cacheable(value = "popularPosts")
    public List<Post> getPopularPosts() {
        log.info("DB에서 인기 게시글 조회 후 캐시 저장");
        return postRepository.findTop5ByOrderByViewsDesc();
    }
}
