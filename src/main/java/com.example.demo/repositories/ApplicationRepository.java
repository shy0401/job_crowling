package com.example.demo.repositories;

import com.example.demo.models.Application;
import com.example.demo.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // 사용자와 채용 공고로 지원 정보 조회
    Optional<Application> findByUserIdAndJobId(Long userId, Long jobId);

    // 사용자별 지원 목록 페이징 처리
    Page<Application> findByUser(User user, Pageable pageable);

    // 사용자와 상태별 지원 목록 조회
    Page<Application> findByUserAndStatus(User user, String status, Pageable pageable);

    // 사용자 ID와 지원 ID로 지원 정보 조회
    Optional<Application> findByIdAndUserId(Long id, Long userId);

    Page<Application> findAllByUser(User user, Pageable pageable);

    // 사용자별 상태 기반 지원 내역 조회
    Page<Application> findAllByUserAndStatus(User user, String status, Pageable pageable);

    // 중복 지원 확인
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
}
