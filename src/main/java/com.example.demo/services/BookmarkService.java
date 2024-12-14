package com.example.demo.services;

import com.example.demo.models.Bookmark;
import com.example.demo.models.Job;
import com.example.demo.models.User;
import com.example.demo.repositories.BookmarkRepository;
import com.example.demo.repositories.JobRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    // 북마크 추가
    public Map<String, Object> addBookmark(String userEmail, Long jobId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (bookmarkRepository.existsByUserIdAndJobId(user.getId(), jobId)) {
            throw new IllegalArgumentException("Bookmark already exists");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        Bookmark bookmark = new Bookmark(user, job);
        bookmarkRepository.save(bookmark);

        return Map.of(
                "message", "Bookmark added successfully",
                "bookmark", Map.of(
                        "id", bookmark.getId(),
                        "jobId", bookmark.getJob().getId(),
                        "jobTitle", bookmark.getJob().getTitle()
                )
        );
    }

    // 북마크 제거
    public Map<String, Object> removeBookmark(String userEmail, Long jobId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Bookmark bookmark = bookmarkRepository.findByUserIdAndJobId(user.getId(), jobId)
                .orElseThrow(() -> new IllegalArgumentException("Bookmark not found"));

        bookmarkRepository.delete(bookmark);

        return Map.of("message", "Bookmark removed successfully");
    }

    // 북마크 목록 조회
    public Map<String, Object> getBookmarks(String userEmail, int page, int size, String sort, String order) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 정렬 방향 설정
        Sort.Direction direction = Sort.Direction.fromString(order);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sort));

        // 북마크 페이징 조회
        Page<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user, pageable);

        // 결과를 Map 형식으로 반환
        return Map.of(
                "data", bookmarks.getContent().stream().map(bookmark -> Map.of(
                        "id", bookmark.getId(),
                        "jobId", bookmark.getJob().getId(),
                        "jobTitle", bookmark.getJob().getTitle(),
                        "createdAt", bookmark.getCreatedAt()
                )).collect(Collectors.toList()),
                "pagination", Map.of(
                        "currentPage", bookmarks.getNumber() + 1,
                        "totalPages", bookmarks.getTotalPages(),
                        "totalItems", bookmarks.getTotalElements(),
                        "pageSize", bookmarks.getSize()
                )
        );
    }
}
