package com.example.backend_B.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public String index() {
        return "Spring Boot 애플리케이션이 성공적으로 실행되었습니다.";
    }
}

