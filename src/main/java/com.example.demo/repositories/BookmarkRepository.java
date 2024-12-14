package com.example.demo.repositories;

import com.example.demo.models.Bookmark;
import com.example.demo.models.Job;
import com.example.demo.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // 사용자와 채용 공고로 북마크 조회
    Optional<Bookmark> findByUserAndJob(User user, Job job);

    // 사용자 ID와 채용 공고 ID로 북마크 존재 여부 확인
    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    // 사용자별 북마크 목록 페이징 조회
    Page<Bookmark> findAllByUser(User user, Pageable pageable);

    // 사용자 ID와 채용 공고 ID로 북마크 조회
    Optional<Bookmark> findByUserIdAndJobId(Long userId, Long jobId);
}
