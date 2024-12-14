package com.example.demo.repositories;

import com.example.demo.models.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    // 필요에 따라 커스텀 메서드 추가 가능
}
