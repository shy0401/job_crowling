package com.example.demo.services;

import com.example.demo.models.JobPosting;
import com.example.demo.repositories.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    @Autowired
    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    // 필터링된 채용 공고 목록 조회
    public Page<JobPosting> getFilteredAndSortedJobPostings(String filter, PageRequest pageRequest) {
        if (filter == null || filter.isBlank()) {
            return jobPostingRepository.findAll(pageRequest);
        }
        return jobPostingRepository.findByTitleContainingOrCompanyNameContaining(filter, filter, pageRequest);
    }

    // 채용 공고 상세 조회
    public JobPosting getJobPostingById(Long id) {
        return jobPostingRepository.findById(id).orElse(null);
    }

    // 채용 공고 등록
    public JobPosting saveJobPosting(JobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }

    // 채용 공고 수정
    public JobPosting updateJobPosting(Long id, JobPosting updatedJobPosting) {
        return jobPostingRepository.findById(id).map(existingJobPosting -> {
            if (updatedJobPosting.getTitle() != null) existingJobPosting.setTitle(updatedJobPosting.getTitle());
            if (updatedJobPosting.getCompanyName() != null) existingJobPosting.setCompanyName(updatedJobPosting.getCompanyName());
            if (updatedJobPosting.getLocation() != null) existingJobPosting.setLocation(updatedJobPosting.getLocation());
            if (updatedJobPosting.getExperience() != null) existingJobPosting.setExperience(updatedJobPosting.getExperience());
            if (updatedJobPosting.getEducation() != null) existingJobPosting.setEducation(updatedJobPosting.getEducation());
            if (updatedJobPosting.getEmploymentType() != null) existingJobPosting.setEmploymentType(updatedJobPosting.getEmploymentType());
            if (updatedJobPosting.getDueDate() != null) existingJobPosting.setDueDate(updatedJobPosting.getDueDate());
            if (updatedJobPosting.getJobSector() != null) existingJobPosting.setJobSector(updatedJobPosting.getJobSector());
            if (updatedJobPosting.getSalary() != null) existingJobPosting.setSalary(updatedJobPosting.getSalary());
            return jobPostingRepository.save(existingJobPosting);
        }).orElse(null);
    }

    // 채용 공고 삭제
    public boolean deleteJobPosting(Long id) {
        if (jobPostingRepository.existsById(id)) {
            jobPostingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
