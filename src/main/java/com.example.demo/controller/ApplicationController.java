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
    public ResponseEntity<?> applyForJob(
            @RequestBody Map<String, Object> requestBody,
            Authentication authentication) {

        try {
            // 입력 데이터 추출
            Long jobId = Long.valueOf(requestBody.get("jobId").toString());
            String resume = requestBody.getOrDefault("resume", "").toString();

            // 사용자 및 채용 공고 조회
            String userEmail = authentication.getName();
            User user = userService.findByEmail(userEmail).orElseThrow(() ->
                    new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            Job job = jobService.findById(jobId).orElseThrow(() ->
                    new IllegalArgumentException("채용 공고를 찾을 수 없습니다."));

            // 중복 지원 확인
            if (applicationService.checkIfAlreadyApplied(user.getId(), job.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "You have already applied for this job."));
            }

            // 지원 저장
            Application newApplication = new Application(user, job, resume, "Pending");
            applicationService.save(newApplication);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Application submitted successfully.", "application", newApplication));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error", "error", e.getMessage()));
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
            // 사용자 조회
            String userEmail = authentication.getName();
            User user = userService.findByEmail(userEmail).orElseThrow(() ->
                    new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 지원 내역 조회
            var result = applicationService.getApplicationsWithFilters(
                    user,
                    status,
                    sort.orElse("appliedAt"),
                    order.orElse("desc"),
                    page.orElse(1),
                    size.orElse(10)
            );

            // 결과 반환
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error", "error", e.getMessage()));
        }
    }

    // 지원 취소
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelApplication(@PathVariable Long id, Authentication authentication) {
        try {
            // 사용자 조회
            String userEmail = authentication.getName();
            User user = userService.findByEmail(userEmail).orElseThrow(() ->
                    new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 지원 내역 조회
            Application application = applicationService.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("지원 내역을 찾을 수 없습니다."));

            // 상태 확인 및 취소 처리
            if (!application.getStatus().equals("Pending")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Cannot cancel this application as it is not pending."));
            }

            application.setStatus("Cancelled");
            applicationService.save(application);

            return ResponseEntity.ok(Map.of("message", "Application cancelled successfully."));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error", "error", e.getMessage()));
        }
    }
}
