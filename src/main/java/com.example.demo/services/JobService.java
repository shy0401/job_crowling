package com.example.demo.services;

import com.example.demo.models.Job;
import com.example.demo.models.JobPosting;
import com.example.demo.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Page<Job> getFilteredAndSortedJobs(String filter, PageRequest pageRequest) {
        if (filter == null || filter.isBlank()) {
            return jobRepository.findAll(pageRequest);
        }
        return jobRepository.findByLocationContainingOrExperienceContainingOrCompanyContainingOrTitleContaining(
                filter, filter, filter, filter, pageRequest);
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public Job updateJob(Long id, Job updatedJob) {
        return jobRepository.findById(id).map(existingJob -> {
            if (updatedJob.getTitle() != null) existingJob.setTitle(updatedJob.getTitle());
            if (updatedJob.getCompany() != null) existingJob.setCompany(updatedJob.getCompany());
            if (updatedJob.getLocation() != null) existingJob.setLocation(updatedJob.getLocation());
            if (updatedJob.getExperience() != null) existingJob.setExperience(updatedJob.getExperience());
            if (updatedJob.getEducation() != null) existingJob.setEducation(updatedJob.getEducation());
            if (updatedJob.getEmploymentType() != null) existingJob.setEmploymentType(updatedJob.getEmploymentType());
            return jobRepository.save(existingJob);
        }).orElse(null);
    }

    public boolean deleteJob(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }
}
