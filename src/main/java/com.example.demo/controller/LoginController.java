package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 템플릿: src/main/resources/templates/login.html
    }

    @PostMapping("/login")
    public String loginRedirect() {
        return "redirect:/auth/login"; // POST 요청은 /auth/login으로 리다이렉트
    }
}
