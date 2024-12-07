package com.example.demo.controller;

import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            logger.warn("Registration failed: Missing email or password");
            return ResponseEntity.badRequest().body("Email and password must not be empty.");
        }

        try {
            userService.registerUser(email, password);
            logger.info("User registered successfully: {}", email);
            return ResponseEntity.ok("User registered successfully.");
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed for email {}: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            logger.warn("Login failed: Missing email or password");
            return ResponseEntity.badRequest().body("Email and password must not be empty.");
        }

        if (userService.validateUser(email, password)) {
            String token = jwtUtils.generateToken(email);
            logger.info("Login successful for user: {}", email);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            logger.warn("Login failed for user: {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }
}
