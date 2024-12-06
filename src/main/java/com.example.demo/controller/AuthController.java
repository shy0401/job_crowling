package com.example.demo.controller;

import com.example.demo.services.UserService;
import com.example.demo.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        // 입력 검증
        if (!ValidationUtils.isValidEmail(email)) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        if (!ValidationUtils.isValidPassword(password)) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters long and include a special character.");
        }

        try {
            userService.registerUser(email, password);
            return ResponseEntity.ok("User registered successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
}
