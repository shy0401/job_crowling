package com.example.demo.controller;

import com.example.demo.services.BookmarkService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // 북마크 추가/제거
    @PostMapping
    public ResponseEntity<?> toggleBookmark(@RequestBody Map<String, Long> request, Authentication authentication) {
        Long jobPostingId = request.get("jobPostingId");
        String userEmail = authentication.getName();
        return bookmarkService.toggleBookmark(userEmail, jobPostingId);
    }

    // 북마크 목록 조회
    @GetMapping
    public ResponseEntity<?> getBookmarks(Authentication authentication) {
        String userEmail = authentication.getName();
        return bookmarkService.getBookmarks(userEmail);
    }
}
