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
        // 필터 및 정렬 로직 구현
        // 현재는 모든 데이터를 단순 반환하는 예제
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("채용 공고를 찾을 수 없습니다."));
    }
}
