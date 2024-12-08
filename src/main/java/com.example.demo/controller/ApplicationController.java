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

import java.util.Optional;

@RestController
@RequestMapping("saramin_data.applications")
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
    public ResponseEntity<?> applyForJob(@RequestParam Long jobId,
                                         Authentication authentication,
                                         @RequestParam(required = false) String resume) {
        String userEmail = authentication.getName();
        User user = userService.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Job job = jobService.findById(jobId).orElseThrow(() ->
                new IllegalArgumentException("채용 공고를 찾을 수 없습니다."));

        if (applicationService.checkIfAlreadyApplied(user.getId(), job.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 지원한 공고입니다.");
        }

        applicationService.applyForJob(new Application(user, job, resume));
        return ResponseEntity.status(HttpStatus.CREATED).body("지원이 완료되었습니다.");
    }

    // 지원 내역 조회
    @GetMapping
    public ResponseEntity<?> getApplications(@RequestParam Optional<String> status,
                                             @RequestParam Optional<Integer> page,
                                             @RequestParam Optional<Integer> size,
                                             Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userService.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        var result = applicationService.getApplicationsWithFilters(user, status, page.orElse(1), size.orElse(10));
        return ResponseEntity.ok(result);
    }

    // 지원 취소
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelApplication(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userService.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        boolean isCancelled = applicationService.cancelApplication(id, user.getId());
        if (isCancelled) {
            return ResponseEntity.ok("지원이 취소되었습니다.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("지원 내역을 찾을 수 없습니다.");
    }
}
