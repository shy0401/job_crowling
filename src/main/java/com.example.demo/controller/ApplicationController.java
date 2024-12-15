package com.example.demo.controller;

import com.example.demo.models.Application;
import com.example.demo.models.Job;
import com.example.demo.models.User;
import com.example.demo.services.ApplicationService;
import com.example.demo.services.JobService;
import com.example.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final UserService userService;
    private final JobService jobService;

    public ApplicationController(ApplicationService applicationService, UserService userService, JobService jobService) {
        this.applicationService = applicationService;
        this.userService = userService;
        this.jobService = jobService;
    }

    // 지원하기
    @PostMapping
    public ResponseEntity<?> applyForJob(@RequestBody Map<String, Object> requestBody, Authentication authentication) {
        try {
            Long jobId = Long.valueOf(requestBody.get("jobId").toString());
            String resume = requestBody.getOrDefault("resume", "").toString();

            User user = getAuthenticatedUser(authentication);
            Job job = jobService.findById(jobId).orElseThrow(() ->
                    new IllegalArgumentException("해당 채용 공고를 찾을 수 없습니다."));

            Application application = applicationService.applyForJob(user, job, resume);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "지원이 성공적으로 제출되었습니다.", "applicationId", application.getId()));
        } catch (Exception e) {
            return handleError(e);
        }
    }

    // 지원 내역 조회
    @GetMapping
    public ResponseEntity<?> getApplications(
            @RequestParam Optional<String> status,
            @RequestParam Optional<String> sort,
            @RequestParam Optional<String> order,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            Authentication authentication) {

        try {
            User user = getAuthenticatedUser(authentication);

            var result = applicationService.getApplicationsWithFilters(
                    user,
                    status,
                    sort.orElse("appliedAt"),
                    order.orElse("desc"),
                    page.orElse(1),
                    size.orElse(10)
            );

            return ResponseEntity.ok(Map.of(
                    "data", result.getContent(),
                    "pagination", Map.of(
                            "currentPage", result.getNumber() + 1,
                            "totalPages", result.getTotalPages(),
                            "totalItems", result.getTotalElements(),
                            "pageSize", result.getSize()
                    )
            ));
        } catch (Exception e) {
            return handleError(e);
        }
    }

    // 지원 취소
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelApplication(@PathVariable Long id, Authentication authentication) {
        try {
            User user = getAuthenticatedUser(authentication);
            applicationService.cancelApplication(user.getId(), id);

            return ResponseEntity.ok(Map.of("message", "지원이 취소되었습니다."));
        } catch (Exception e) {
            return handleError(e);
        }
    }

    // 인증된 사용자 가져오기
    private User getAuthenticatedUser(Authentication authentication) {
        String userEmail = authentication.getName();
        return userService.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // 에러 처리
    private ResponseEntity<?> handleError(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "서버 오류가 발생했습니다.", "error", e.getMessage()));
    }
}
