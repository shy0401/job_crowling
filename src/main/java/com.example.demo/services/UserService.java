package com.example.demo.services;

import com.example.demo.models.LoginHistory;
import com.example.demo.models.User;
import com.example.demo.repositories.LoginHistoryRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       LoginHistoryRepository loginHistoryRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.loginHistoryRepository = loginHistoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 이메일 존재 여부 확인
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // 이메일로 사용자 조회
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 사용자 저장
    public User save(User user) {
        return userRepository.save(user);
    }

    // 회원가입 처리
    @Transactional
    public void registerUser(String email, String password, String name) {
        if (existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setName(name);
        save(newUser);
    }

    // 로그인 유효성 검사 및 로그인 기록 저장
    public boolean validateUser(String email, String password, String ip, String device) {
        Optional<User> optionalUser = findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                // 로그인 성공 시 로그인 기록 저장
                saveLoginHistory(new LoginHistory(user, LocalDateTime.now(), ip, device));
                return true;
            }
        }
        return false;
    }

    // 사용자 프로필 업데이트
    @Transactional
    public void updateProfile(Long userId, String name, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
    }

    // 사용자 프로필 조회
    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        userRepository.deleteById(userId);
    }

    // 로그인 기록 저장
    public void saveLoginHistory(LoginHistory loginHistory) {
        loginHistoryRepository.save(loginHistory);
    }

    // Refresh Token으로 사용자 조회
    public Optional<User> findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }
}
