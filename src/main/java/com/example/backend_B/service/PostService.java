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

    public List<Post> index() {
        return postRepository.findAll();
    }

    // 단일 게시글 조회 + 조회수 증가 + 캐시 무효화
    @CacheEvict(value = {"post", "popularPosts"}, allEntries = true)
    public Post show(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.incrementViews();         // 조회수 증가
            postRepository.save(post);     // DB에 반영
        }
        return post;
    }

    @CacheEvict(value = {"popularPosts"}, allEntries = true)
    public Post create(PostForm dto) {
        Post post = dto.toEntity();
        return postRepository.save(post);
    }

    @CacheEvict(value = {"post", "popularPosts"}, allEntries = true)
    public Post update(Long id, PostForm dto) {
        Post target = postRepository.findById(id).orElse(null);
        if (target != null) {
            target.patch(dto.toEntity());
            return postRepository.save(target);
        }
        return null;
    }

    @CacheEvict(value = {"post", "popularPosts"}, allEntries = true)
    public Post delete(Long id) {
        Post target = postRepository.findById(id).orElse(null);
        if (target != null) {
            postRepository.delete(target);
            return target;
        }
        return null;
    }

    @Transactional
    @CacheEvict(value = {"popularPosts"}, allEntries = true)
    public List<Post> createArticles(List<PostForm> dtos) {
        return dtos.stream()
                .map(PostForm::toEntity)
                .map(postRepository::save)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "popularPosts")
    public List<Post> getPopularPosts() {
        log.info("DB에서 인기 게시글 조회 후 캐시 저장");
        return postRepository.findTop5ByOrderByViewsDesc();
    }
}
