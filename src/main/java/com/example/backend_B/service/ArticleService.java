package com.example.backend_B.service;

import com.example.backend_B.dto.ArticleForm;
import com.example.backend_B.entity.Article;
import com.example.backend_B.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String ARTICLE_CACHE_PREFIX = "article::";

    public Article getArticleById(Long id) {
        String key = ARTICLE_CACHE_PREFIX + id;
        Article cachedArticle = (Article) redisTemplate.opsForValue().get(key);
        if (cachedArticle != null) {
            log.info("Redis에서 캐시된 게시글 반환");
            return cachedArticle;
        }

        Article article = articleRepository.findById(id).orElse(null);
        if (article != null) {
            article.incrementViews();
            articleRepository.save(article);
            redisTemplate.opsForValue().set(key, article);
        }

        return article;
    }

    public List<Article> getPopularArticles() {
        String key = "article::popular";
        List<Article> cached = (List<Article>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            log.info("Redis에서 인기 게시글 캐시 반환");
            return cached;
        }

        List<Article> popular = articleRepository.findTop5ByOrderByViewsDesc();
        redisTemplate.opsForValue().set(key, popular, Duration.ofMinutes(10));
        log.info("DB에서 인기 게시글 조회 후 캐시 저장");

        return popular;
    }

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        if (article.getId() != null) return null;
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        Article article = dto.toEntity();
        log.info("id: {}, article: {}", id, article.toString());

        Article target = articleRepository.findById(id).orElse(null);
        if (target == null || id != article.getId()) {
            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
            return null;
        }

        target.patch(article);
        return articleRepository.save(target);
    }

    public Article delete(Long id) {
        Article target = articleRepository.findById(id).orElse(null);
        if (target == null) return null;
        articleRepository.delete(target);
        return target;
    }

    @Transactional
    public List<Article> createArticles(List<ArticleForm> dtos) {
        List<Article> articleList = dtos.stream()
                .map(ArticleForm::toEntity)
                .collect(Collectors.toList());

        articleList.forEach(articleRepository::save);

        articleRepository.findById(-1L)
                .orElseThrow(() -> new IllegalArgumentException("결제 실패!"));

        return articleList;
    }
}
