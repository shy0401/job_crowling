package com.example.demo.repositories;

import com.example.demo.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByLocationContainingOrExperienceContainingOrCompanyContainingOrTitleContaining(
            String location, String experience, String company, String title, Pageable pageable);
}
