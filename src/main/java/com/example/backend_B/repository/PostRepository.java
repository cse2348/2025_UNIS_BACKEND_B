package com.example.backend_B.repository;

import com.example.backend_B.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByOrderByViewsDesc();
    List<Post> findTop5ByOrderByViewsDesc();
}
