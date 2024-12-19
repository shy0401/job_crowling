package com.example.demo.controllers;

import com.example.demo.models.Resume;
import com.example.demo.models.User;
import com.example.demo.services.ResumeService;
import com.example.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;

    public ResumeController(ResumeService resumeService, UserService userService) {
        this.resumeService = resumeService;
        this.userService = userService;
    }

    // 지원서 작성
    @PostMapping
    public ResponseEntity<?> createResume(@RequestBody Map<String, String> requestBody, Authentication authentication) {
        String title = requestBody.get("title");
        String content = requestBody.get("content");
        String userEmail = authentication.getName();

        User user = userService.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Resume resume = resumeService.createResume(user, title, content);

        return ResponseEntity.status(201).body(Map.of("message", "Resume created successfully", "resume", resume));
    }

    // 지원서 조회
    @GetMapping
    public ResponseEntity<?> getResumes(Authentication authentication) {
        String userEmail = authentication.getName();

        User user = userService.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Resume> resumes = resumeService.getResumesByUser(user);

        return ResponseEntity.ok(Map.of("resumes", resumes));
    }

    // 지원서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();

        User user = userService.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다.."));

        Resume resume = resumeService.getResumeById(id).orElseThrow(() ->
                new IllegalArgumentException("Resume not found"));

        if (!resume.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body(Map.of("message", "You are not authorized to delete this resume"));
        }

        resumeService.deleteResume(resume);

        return ResponseEntity.ok(Map.of("message", "Resume deleted successfully"));
    }
}
