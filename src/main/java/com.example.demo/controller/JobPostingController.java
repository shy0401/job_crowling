package com.example.demo.controller;

import com.example.demo.models.JobPosting;
import com.example.demo.services.JobPostingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobpostings")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    // 채용 공고 목록 조회 + 필터링 + 정렬 + 페이지네이션
    @GetMapping
    public ResponseEntity<Page<JobPosting>> getAllJobPostings(
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, direction.equalsIgnoreCase("desc") ?
                Sort.by(sort).descending() : Sort.by(sort).ascending());

        Page<JobPosting> jobPostings = jobPostingService.getFilteredAndSortedJobPostings(filter, pageRequest);
        return ResponseEntity.ok(jobPostings);
    }

    // 채용 공고 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getJobPostingById(@PathVariable Long id) {
        JobPosting jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobPosting);
    }

    // 채용 공고 등록
    @PostMapping
    public ResponseEntity<JobPosting> createJobPosting(@RequestBody JobPosting jobPosting) {
        JobPosting savedJobPosting = jobPostingService.saveJobPosting(jobPosting);
        return ResponseEntity.status(201).body(savedJobPosting);
    }

    // 채용 공고 수정
    @PutMapping("/{id}")
    public ResponseEntity<JobPosting> updateJobPosting(@PathVariable Long id, @RequestBody JobPosting updatedJobPosting) {
        JobPosting jobPosting = jobPostingService.updateJobPosting(id, updatedJobPosting);
        if (jobPosting == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobPosting);
    }

    // 채용 공고 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long id) {
        boolean isDeleted = jobPostingService.deleteJobPosting(id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
