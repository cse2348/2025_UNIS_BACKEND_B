package com.example.backend_B.controller;

import com.example.backend_B.dto.PostForm;
import com.example.backend_B.dto.CommentDto;
import com.example.backend_B.entity.Post;
import com.example.backend_B.repository.PostRepository;
import com.example.backend_B.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;

    @GetMapping("/posts/new")
    public String newPostForm() {
        return "posts/new";
    }

    @PostMapping("/posts/create")
    public String createPost(PostForm form) {
        Post post = form.toEntity();
        Post saved = postRepository.save(post);
        return "redirect:/posts/" + saved.getId();
    }

    @GetMapping("/posts/{id}")
    public String show(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.incrementViews();
            postRepository.save(post);
        }
        List<CommentDto> commentDtos = commentService.comments(id);
        model.addAttribute("post", post);
        model.addAttribute("commentDtos", commentDtos);
        return "posts/show";
    }

    @GetMapping("/posts")
    public String index(@RequestParam(value = "sort", required = false) String sort, Model model) {
        List<Post> postList;
        if ("views".equals(sort)) {
            postList = postRepository.findAllByOrderByViewsDesc();
            model.addAttribute("isViewSort", true);
        } else {
            postList = postRepository.findAllByOrderByCreatedAtDesc();
            model.addAttribute("isCreatedSort", true);
        }
        model.addAttribute("postList", postList);
        return "posts/index";
    }

    @GetMapping("/posts/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        model.addAttribute("post", post);
        return "posts/edit";
    }

    @PostMapping("/posts/update")
    public String update(PostForm form) {
        Post post = form.toEntity();
        Post target = postRepository.findById(post.getId()).orElse(null);
        if (target != null) {
            target.patch(post);
            postRepository.save(target);
        }
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Post target = postRepository.findById(id).orElse(null);
        if (target != null) {
            postRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        return "redirect:/posts";
    }

    @GetMapping("/posts/popular")
    public String popular(Model model) {
        List<Post> postList = postRepository.findTop5ByOrderByViewsDesc();
        model.addAttribute("postList", postList);
        return "posts/popular";
    }
}
