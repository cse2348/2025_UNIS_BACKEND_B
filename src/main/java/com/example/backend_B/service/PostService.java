package com.example.backend_B.service;

import com.example.backend_B.dto.PostForm;
import com.example.backend_B.entity.Post;
import com.example.backend_B.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 전체 게시글 목록 조회
    public List<Post> index() {
        return postRepository.findAll();
    }

    // 단일 게시글 조회 (Redis 캐시 적용)
    @Cacheable(value = "post", key = "#id")
    public Post show(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    // 게시글 생성 시 인기 캐시 초기화
    @CacheEvict(value = {"popularPosts"}, allEntries = true)
    public Post create(PostForm dto) {
        Post post = dto.toEntity();
        return postRepository.save(post);
    }

    // 게시글 수정 시 캐시 초기화
    @CacheEvict(value = {"post", "popularPosts"}, allEntries = true)
    public Post update(Long id, PostForm dto) {
        Post target = postRepository.findById(id).orElse(null);
        if (target != null) {
            target.patch(dto.toEntity());
            return postRepository.save(target);
        }
        return null;
    }

    // 게시글 삭제 시 캐시 초기화
    @CacheEvict(value = {"post", "popularPosts"}, allEntries = true)
    public Post delete(Long id) {
        Post target = postRepository.findById(id).orElse(null);
        if (target != null) {
            postRepository.delete(target);
            return target;
        }
        return null;
    }

    // 트랜잭션을 이용한 다수 게시글 생성 (캐시 초기화 포함)
    @Transactional
    @CacheEvict(value = {"popularPosts"}, allEntries = true)
    public List<Post> createArticles(List<PostForm> dtos) {
        return dtos.stream()
                .map(PostForm::toEntity)
                .map(postRepository::save)
                .collect(Collectors.toList());
    }

    // 인기 게시글 조회 (Redis 캐시 적용)
    @Cacheable(value = "popularPosts")
    public List<Post> getPopularPosts() {
        log.info("DB에서 인기 게시글 조회 후 캐시 저장");
        return postRepository.findTop5ByOrderByViewsDesc();
    }
}
