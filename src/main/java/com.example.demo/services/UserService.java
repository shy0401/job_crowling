package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 사용자 ID를 기반으로 사용자 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 정보
     * @throws IllegalArgumentException 사용자 정보를 찾을 수 없을 때
     */
    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    /**
     * 회원가입 메서드
     *
     * @param email    사용자의 이메일
     * @param password 사용자의 비밀번호
     * @throws IllegalArgumentException 이메일이 이미 존재할 때
     */
    public void registerUser(String email, String password) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        // 새 사용자 생성 및 비밀번호 암호화
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화

        // 사용자 저장
        userRepository.save(user);
    }

    /**
     * 로그인 시 이메일과 비밀번호를 확인하는 메서드
     *
     * @param email       사용자의 이메일
     * @param rawPassword 사용자가 입력한 비밀번호
     * @return 인증 성공 여부
     */
    public boolean validateUser(String email, String rawPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }
}
