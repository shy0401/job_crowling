package com.example.demo.controller;

import com.example.demo.models.Application;
import com.example.demo.services.ApplicationService;
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

    @PostMapping("/")
    public ResponseEntity<?> applyForJob(@RequestBody Application application, Authentication authentication) {
        String email = authentication.getName();
        applicationService.applyForJob(application, email);
        return ResponseEntity.ok("Application submitted");
    }

    @GetMapping("/")
    public ResponseEntity<List<Application>> getApplications(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getApplications(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelApplication(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        applicationService.cancelApplication(id, email);
        return ResponseEntity.ok("Application canceled");
    }
}
