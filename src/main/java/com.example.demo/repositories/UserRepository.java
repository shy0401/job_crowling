package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);
}
