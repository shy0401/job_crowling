package com.example.demo.repositories;

import com.example.demo.models.Bookmark;
import com.example.demo.models.Job;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // 사용자와 채용 공고로 북마크 조회
    Optional<Bookmark> findByUserAndJob(User user, Job job);

    // 사용자별 북마크 목록 조회
    List<Bookmark> findByUser(User user);
}
