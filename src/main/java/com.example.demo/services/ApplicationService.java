package com.example.demo.services;

import com.example.demo.models.Application;
import com.example.demo.models.Job;
import com.example.demo.models.User;
import com.example.demo.repositories.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    // 사용자에 대한 전체 지원 내역 조회
    public List<Application> getApplications(User user) {
        // Pageable 없이 전체 리스트 조회
        return applicationRepository.findByUser(user, Pageable.unpaged()).getContent();
    }

    // 사용자와 채용 공고로 지원 여부 확인
    public Optional<Application> findByUserAndJob(Long userId, Long jobId) {
        return applicationRepository.findByUserIdAndJobId(userId, jobId);
    }

    // 채용 공고에 지원
    public void applyForJob(Application application) {
        applicationRepository.save(application);
    }

    // 지원 취소
    public boolean cancelApplication(Long applicationId, Long userId) {
        Optional<Application> application = applicationRepository.findByIdAndUserId(applicationId, userId);
        if (application.isPresent()) {
            applicationRepository.delete(application.get());
            return true;
        }
        return false;
    }

    // 이미 지원 여부 확인
    public boolean checkIfAlreadyApplied(Long userId, Long jobId) {
        return applicationRepository.findByUserIdAndJobId(userId, jobId).isPresent();
    }

    // 필터와 페이지네이션을 적용한 지원 내역 조회
    public Object getApplicationsWithFilters(User user, Optional<String> status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (status.isPresent()) {
            Page<Application> filteredApplications = applicationRepository.findByUserAndStatus(user, status.get(), pageable);
            return filteredApplications.map(application -> Map.of(
                    "id", application.getId(),
                    "job", application.getJob(),
                    "status", application.getStatus(),
                    "resume", application.getResume(),
                    "appliedAt", application.getAppliedAt()
            ));
        }

        Page<Application> applications = applicationRepository.findByUser(user, pageable);
        return applications.map(application -> Map.of(
                "id", application.getId(),
                "job", application.getJob(),
                "status", application.getStatus(),
                "resume", application.getResume(),
                "appliedAt", application.getAppliedAt()
        ));
    }
}
