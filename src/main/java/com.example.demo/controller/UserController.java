package com.example.demo.controller;

import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 회원 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        // 인증된 사용자의 ID를 가져오는 로직 추가 필요
        Long userId = 1L; // 예제용 사용자 ID
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }
}
