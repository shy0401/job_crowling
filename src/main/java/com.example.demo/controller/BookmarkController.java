package com.example.demo.controller;

import com.example.demo.services.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // 북마크 추가
    @PostMapping("/add")
    public ResponseEntity<?> addBookmark(@RequestBody Map<String, Long> request, Authentication authentication) {
        Long jobPostingId = request.get("jobPostingId");

        if (jobPostingId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Job posting ID is required."));
        }

        String userEmail = authentication.getName();

        try {
            return ResponseEntity.ok(bookmarkService.addBookmark(userEmail, jobPostingId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error", "error", e.getMessage()));
        }
    }

    // 북마크 제거
    @PostMapping("/remove")
    public ResponseEntity<?> removeBookmark(@RequestBody Map<String, Long> request, Authentication authentication) {
        Long jobPostingId = request.get("jobPostingId");

        if (jobPostingId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Job posting ID is required."));
        }

        String userEmail = authentication.getName();

        try {
            return ResponseEntity.ok(bookmarkService.removeBookmark(userEmail, jobPostingId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error", "error", e.getMessage()));
        }
    }

    // 북마크 목록 조회
    @GetMapping
    public ResponseEntity<?> getBookmarks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order,
            Authentication authentication) {

        if (page < 1 || size < 1) {
            return ResponseEntity.badRequest().body(Map.of("message", "Page and size must be greater than 0."));
        }

        String userEmail = authentication.getName();

        try {
            Map<String, Object> bookmarks = bookmarkService.getBookmarks(userEmail, page, size, sort, order);
            return ResponseEntity.ok(bookmarks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error", "error", e.getMessage()));
        }
    }
}
