package com.example.demo.services;

import com.example.demo.models.Application;
import com.example.demo.models.Job;
import com.example.demo.models.User;
import com.example.demo.repositories.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // 전체 지원 내역 조회 (페이징 없음)
    public List<Application> getApplications(User user) {
        return applicationRepository.findByUser(user, Pageable.unpaged()).getContent();
    }

    // 특정 사용자와 채용 공고에 대한 지원 내역 확인
    public Optional<Application> findByUserAndJob(Long userId, Long jobId) {
        return applicationRepository.findByUserIdAndJobId(userId, jobId);
    }

    // 지원 정보 저장
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

    // 중복 지원 확인
    public boolean checkIfAlreadyApplied(Long userId, Long jobId) {
        return applicationRepository.findByUserIdAndJobId(userId, jobId).isPresent();
    }

    // 필터와 페이지네이션을 적용한 지원 내역 조회
    public Object getApplicationsWithFilters(User user, Optional<String> status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (status.isPresent()) {
            Page<Application> filteredApplications = applicationRepository.findByUserAndStatus(user, status.get(), pageable);
            return mapApplications(filteredApplications);
        }

        Page<Application> applications = applicationRepository.findByUser(user, pageable);
        return mapApplications(applications);
    }

    // 지원 내역 조회 결과 매핑
    private Page<Map<String, Object>> mapApplications(Page<Application> applications) {
        return applications.map(application -> Map.of(
                "id", application.getId(),
                "job", Map.of(
                        "id", application.getJob().getId(),
                        "title", application.getJob().getTitle()
                ),
                "status", application.getStatus(),
                "resume", application.getResume(),
                "appliedAt", application.getAppliedAt()
        ));
    }

    // 지원 내역 저장
    public void save(Application application) {
        applicationRepository.save(application);
    }

    // 필터를 적용한 지원 내역 조회
    public Page<Application> getApplicationsWithFilters(User user, Optional<String> status, String sortBy, String sortDirection, int page, int size) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);

        if (status.isPresent()) {
            return applicationRepository.findAllByUserAndStatus(user, status.get(), pageRequest);
        } else {
            return applicationRepository.findAllByUser(user, pageRequest);
        }
    }

    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }
}
