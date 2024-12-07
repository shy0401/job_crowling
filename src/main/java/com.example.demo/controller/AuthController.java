package com.example.demo.controller;

import com.example.demo.models.User;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import java.util.Map;
import java.util.Optional;

@Controller  // Controller로 수정하여 HTML 페이지를 렌더링
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    // 회원가입 페이지 (GET)
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";  // register.html을 렌더링
    }

    // 회원가입 처리 (POST)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String email,
                                          @RequestParam String password,
                                          @RequestParam String name) {

        if (email == null || email.isBlank() || password == null || password.isBlank() || name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("모든 필드를 입력해주세요.");
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setName(name);

        userService.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email,
                                       @RequestParam String password) {
        // 이메일과 비밀번호 검증
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtils.generateToken(user.getEmail());
        return ResponseEntity.ok(Map.of("accessToken", token));
    }


    // 회원 정보 조회
// AuthController.java

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "name", user.getName()
        ));
    }


    @DeleteMapping("/deleteAccount")
    public ResponseEntity<?> deleteAccount(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();
        userService.deleteUser(user.getId()); // 사용자 삭제

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("회원 탈퇴가 완료되었습니다.");
    }

    // 회원 정보 수정
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody Map<String, String> updates) {
        String email = authentication.getName();
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();
        String name = updates.get("name");
        String password = updates.get("password");

        if (name != null && !name.isBlank()) {
            user.setName(name);
        }

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userService.save(user);
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }
}
