package com.example.backend_B.repository;

import com.example.backend_B.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    @Override
    ArrayList<Post> findAll();
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByOrderByViewsDesc();
    List<Post> findTop5ByOrderByViewsDesc(); //상위 5개만찾음.
    
}

