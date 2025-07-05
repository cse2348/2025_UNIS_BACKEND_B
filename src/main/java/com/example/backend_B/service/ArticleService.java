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

    @CacheEvict(value = {"popularArticles"}, allEntries = true)
    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        return articleRepository.save(article);
    }

    @CacheEvict(value = {"article", "popularArticles"}, key = "#id", allEntries = true)
    public Article update(Long id, ArticleForm dto) {
        Article target = articleRepository.findById(id).orElse(null);
        if (target != null) {
            target.patch(dto.toEntity());
            return articleRepository.save(target);
        }
        return null;
    }

    @CacheEvict(value = {"article", "popularArticles"}, key = "#id", allEntries = true)
    public Article delete(Long id) {
        Article target = articleRepository.findById(id).orElse(null);
        if (target != null) {
            articleRepository.delete(target);
            return target;
        }
        return null;
    }

    @Transactional
    @CacheEvict(value = {"popularArticles"}, allEntries = true)
    public List<Article> createArticles(List<ArticleForm> dtos) {
        return dtos.stream()
                .map(ArticleForm::toEntity)
                .map(articleRepository::save)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "popularArticles")
    public List<Article> getPopularArticles() {
        log.info("DB에서 인기 게시글 조회 후 캐시 저장");
        return articleRepository.findTop5ByOrderByViewsDesc();
    }
}
