package com.example.backend_B.repository;

import com.example.backend_B.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByCreatedAtDesc();
    List<Article> findAllByOrderByViewsDesc();
    List<Article> findTop5ByOrderByViewsDesc();
}
