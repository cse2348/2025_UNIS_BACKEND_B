package com.example.backend_B.controller;

import com.example.backend_B.dto.ArticleForm;
import com.example.backend_B.dto.CommentDto;
import com.example.backend_B.entity.Article;
import com.example.backend_B.repository.ArticleRepository;
import com.example.backend_B.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentService commentService;

    @GetMapping("/posts/new")
    public String newArticleForm() {
        return "posts/new";
    }

    @PostMapping("/posts/create")
    public String createArticle(ArticleForm form) {
        Article article = form.toEntity();
        Article saved = articleRepository.save(article);
        return "redirect:/posts/" + saved.getId();
    }

    @GetMapping("/posts/{id}")
    public String show(@PathVariable Long id, Model model) {
        Article articleEntity = articleRepository.findById(id).orElse(null);

        if (articleEntity != null) {
            articleEntity.incrementViews();
            articleRepository.save(articleEntity);
        }

        List<CommentDto> commentsDtos = commentService.comments(id);
        model.addAttribute("article", articleEntity);
        model.addAttribute("commentDtos", commentsDtos);
        return "posts/show";
    }

    @GetMapping("/posts")
    public String index(@RequestParam(value = "sort", required = false) String sort, Model model) {
        List<Article> articleEntityList;
        if ("views".equals(sort)) {
            articleEntityList = articleRepository.findAllByOrderByViewsDesc();
            model.addAttribute("isViewSort", true);
        } else {
            articleEntityList = articleRepository.findAllByOrderByCreatedAtDesc();
            model.addAttribute("isCreatedSort", true);
        }
        model.addAttribute("articleList", articleEntityList);
        return "posts/index";
    }

    @GetMapping("/posts/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Article articleEntity = articleRepository.findById(id).orElse(null);
        model.addAttribute("article", articleEntity);
        return "posts/edit";
    }

    @PostMapping("/posts/update")
    public String update(ArticleForm form) {
        Article articleEntity = form.toEntity();
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        if (target != null) {
            target.patch(articleEntity);
            articleRepository.save(target);
        }
        return "redirect:/posts/" + articleEntity.getId();
    }

    @GetMapping("/posts/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Article target = articleRepository.findById(id).orElse(null);
        if (target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        return "redirect:/posts";
    }

    @GetMapping("/posts/popular")
    public String popular(Model model) {
        List<Article> articleEntityList = articleRepository.findTop5ByOrderByViewsDesc();
        model.addAttribute("articleList", articleEntityList);
        return "posts/popular";
    }
}
