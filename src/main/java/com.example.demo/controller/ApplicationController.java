package com.example.demo.controller;

import com.example.demo.models.Application;
import com.example.demo.models.User;
import com.example.demo.services.ApplicationService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<Application>> getApplications(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Application> applications = applicationService.getApplications(user);
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/")
    public ResponseEntity<?> applyForJob(@RequestBody Application application, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        applicationService.applyForJob(application, user);
        return ResponseEntity.ok("Application submitted successfully.");
    }
}
