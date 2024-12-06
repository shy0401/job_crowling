package com.example.demo.controllers;

import com.example.demo.models.Job;
import com.example.demo.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/")
    public ResponseEntity<List<Job>> getJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String location,
            @RequestParam(defaultValue = "") String experience,
            @RequestParam(defaultValue = "asc") String sort
    ) {
        return ResponseEntity.ok(jobService.getJobs(page, limit, search, location, experience, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PostMapping("/")
    public ResponseEntity<?> createJob(@RequestBody Job job) {
        jobService.createJob(job);
        return ResponseEntity.ok("Job created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody Job job) {
        jobService.updateJob(id, job);
        return ResponseEntity.ok("Job updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok("Job deleted");
    }
}
