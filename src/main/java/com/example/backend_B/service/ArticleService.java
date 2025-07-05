package com.example.backend_B.service;

import com.example.backend_B.dto.ArticleForm;
import com.example.backend_B.entity.Article;
import com.example.backend_B.repository.ArticleRepository;
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
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<Article> index() {
        return articleRepository.findAll();
    }

    @Cacheable(value = "article", key = "#id")
    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        return articleRepository.save(article);
    }

    @CacheEvict(value = "article", key = "#id")
    public Article update(Long id, ArticleForm dto) {
        Article target = articleRepository.findById(id).orElse(null);
        if (target != null) {
            target.patch(dto.toEntity());
            return articleRepository.save(target);
        }
        return null;
    }

    @CacheEvict(value = "article", key = "#id")
    public Article delete(Long id) {
        Article target = articleRepository.findById(id).orElse(null);
        if (target != null) {
            articleRepository.delete(target);
            return target;
        }
        return null;
    }

    @Transactional
    public List<Article> createArticles(List<ArticleForm> dtos) {
        return dtos.stream()
                .map(ArticleForm::toEntity)
                .map(articleRepository::save)
                .collect(Collectors.toList());
    }

    // 인기 게시글 상위 5개 조회
    public List<Article> getPopularArticles() {
        return articleRepository.findTop5ByOrderByViewsDesc();
    }
}
