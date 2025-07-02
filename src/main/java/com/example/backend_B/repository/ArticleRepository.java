package com.example.backend_B.repository;

import com.example.backend_B.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    @Override
    ArrayList<Article> findAll();
    List<Article> findAllByOrderByCreatedAtDesc();
    List<Article> findAllByOrderByViewsDesc();
    List<Article> findTop5ByOrderByViewsDesc(); //상위 5개만찾음.
    
}

