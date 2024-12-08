package com.example.demo.repositories;

import com.example.demo.models.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    // 필터링을 위해 제목 또는 회사 이름으로 검색
    Page<JobPosting> findByTitleContainingOrCompanyNameContaining(String title, String companyName, PageRequest pageRequest);
}
