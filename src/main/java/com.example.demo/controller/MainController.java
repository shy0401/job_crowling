package com.example.demo.controller;

import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/main")
    public String mainPage(Model model, Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);

            userOptional.ifPresent(user -> model.addAttribute("user", user));
        }

        return "main"; // templates/main.html 파일 렌더링
    }
}
