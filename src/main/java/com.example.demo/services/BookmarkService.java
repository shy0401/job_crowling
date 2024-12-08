package com.example.demo.services;

import com.example.demo.models.Bookmark;
import com.example.demo.models.Job;
import com.example.demo.models.User;
import com.example.demo.repositories.BookmarkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final JobService jobService;

    public BookmarkService(BookmarkRepository bookmarkRepository, UserService userService, JobService jobService) {
        this.bookmarkRepository = bookmarkRepository;
        this.userService = userService;
        this.jobService = jobService;
    }

    // 북마크 추가/제거
    public ResponseEntity<String> toggleBookmark(String userEmail, Long jobId) {
        // 사용자 조회
        User user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        // 채용 공고 조회
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("채용 공고를 찾을 수 없습니다.");
        }

        // 기존 북마크 존재 여부 확인
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserAndJob(user, job);
        if (existingBookmark.isPresent()) {
            // 북마크 제거
            bookmarkRepository.delete(existingBookmark.get());
            return ResponseEntity.ok("북마크가 제거되었습니다.");
        } else {
            // 북마크 추가
            Bookmark bookmark = new Bookmark(user, job);
            bookmarkRepository.save(bookmark);
            return ResponseEntity.status(HttpStatus.CREATED).body("북마크가 추가되었습니다.");
        }
    }

    // 북마크 목록 조회
    public ResponseEntity<List<Bookmark>> getBookmarks(String userEmail) {
        // 사용자 조회
        User user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 북마크 목록 반환
        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user);
        return ResponseEntity.ok(bookmarks);
    }
}
