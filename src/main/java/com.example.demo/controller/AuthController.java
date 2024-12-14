package com.example.demo.controller;

import com.example.demo.models.User;
import com.example.demo.models.LoginHistory;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
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

    // 회원가입 처리 (POST)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userDetails) {
        String email = userDetails.get("email");
        String password = userDetails.get("password");
        String name = userDetails.get("name");

        if (email == null || email.isBlank() || password == null || password.isBlank() || name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("이메일, 비밀번호, 이름을 모두 입력해주세요.");
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }

        // Base64 암호화
        String encodedPassword = new String(java.util.Base64.getEncoder().encode(password.getBytes()));

        // User 객체 생성 및 저장
        User newUser = new User(email, encodedPassword, name);
        userService.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    // 로그인 처리 (POST)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginDetails) {
        String email = loginDetails.get("email");
        String password = loginDetails.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body("이메일과 비밀번호를 모두 입력해주세요.");
        }

        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        User user = userOptional.get();

        // Base64로 저장된 비밀번호 디코딩 및 검증
        String decodedPassword = new String(java.util.Base64.getDecoder().decode(user.getPassword()));

        if (!decodedPassword.equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 발급
        String accessToken = jwtUtils.generateToken(user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        // Refresh Token 저장
        user.setRefreshToken(refreshToken);
        userService.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "로그인이 성공적으로 완료되었습니다.",
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    // 토큰 갱신 (POST)
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> tokenDetails) {
        String refreshToken = tokenDetails.get("refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh Token을 제공해주세요.");
        }

        Optional<User> userOptional = userService.findByRefreshToken(refreshToken);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 Refresh Token입니다.");
        }

        User user = userOptional.get();

        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token이 만료되었거나 유효하지 않습니다.");
        }

        String newAccessToken = jwtUtils.generateToken(user.getEmail());
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    // 회원 정보 수정 (PUT)
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody Map<String, String> updates) {
        // 인증 객체가 null인지 확인
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
        }

        String email = authentication.getName(); // 인증된 사용자의 이메일 가져오기
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();
        String newName = updates.get("name");
        String newPassword = updates.get("password");

        if (newName != null && !newName.isBlank()) {
            user.setName(newName);
        }

        if (newPassword != null && !newPassword.isBlank()) {
            String encodedPassword = new String(java.util.Base64.getEncoder().encode(newPassword.getBytes()));
            user.setPassword(encodedPassword);
        }

        userService.save(user);
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }


    // 로그아웃 처리 (POST)
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();
        user.setRefreshToken(null);
        userService.save(user);

        return ResponseEntity.ok("로그아웃이 완료되었습니다.");
    }
}
