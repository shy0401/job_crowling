package com.example.demo.services;

import com.example.demo.models.Job;
import com.example.demo.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public List<Job> getJobs(int page, int limit, String search, String location, String experience, String sort) {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public void createJob(Job job) {
        if (job.getTitle() == null || job.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Job title is required");
        }

        if (job.getDescription() == null || job.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Job description is required");
        }

        if (job.getLocation() == null || job.getLocation().isEmpty()) {
            throw new IllegalArgumentException("Location is required");
        }

        boolean isDuplicate = jobRepository.existsByTitleAndLocation(job.getTitle(), job.getLocation());
        if (isDuplicate) {
            throw new IllegalArgumentException("A job with the same title and location already exists");
        }

        jobRepository.save(job);
    }

    public void updateJob(Long id, Job job) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        existingJob.setTitle(job.getTitle());
        existingJob.setDescription(job.getDescription());
        existingJob.setLocation(job.getLocation());
        existingJob.setExperience(job.getExperience());
        existingJob.setCompany(job.getCompany());

        jobRepository.save(existingJob);
    }

    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found");
        }
        jobRepository.deleteById(id);
    }
}
