package com.example.demo.controller;

import com.example.demo.models.Job;
import com.example.demo.services.JobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // 채용 공고 목록 조회 + 필터링 + 정렬 + 페이지네이션
    @GetMapping
    public ResponseEntity<Page<Job>> getAllJobs(
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Sort sortBy = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortBy);

        Page<Job> jobs = jobService.getFilteredAndSortedJobs(filter, pageRequest);
        return ResponseEntity.ok(jobs);
    }

    // 채용 공고 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }

    // 채용 공고 등록
    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody Job job) {
        Job savedJob = jobService.saveJob(job);
        return ResponseEntity.status(201).body(savedJob);
    }

    // 채용 공고 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
        Job job = jobService.updateJob(id, updatedJob);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }

    // 채용 공고 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        boolean isDeleted = jobService.deleteJob(id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("채용 공고가 삭제되었습니다.");
    }
}
