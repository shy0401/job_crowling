package com.example.demo.services;

import com.example.demo.models.Application;
import com.example.demo.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public void applyForJob(Application application, String email) {
        // 신청하는 사용자의 이메일 설정
        application.setUserEmail(email);
        application.setStatus("PENDING"); // 기본 상태 설정
        applicationRepository.save(application); // 데이터베이스에 저장
    }

    public List<Application> getApplications(String email) {
        // 사용자의 신청 내역 조회
        return applicationRepository.findByUserEmail(email);
    }

    public void cancelApplication(Long id, String email) {
        // 신청 내역 확인 (권한 포함)
        Optional<Application> application = applicationRepository.findByIdAndUserEmail(id, email);

        if (application.isPresent()) {
            applicationRepository.delete(application.get()); // 데이터 삭제
        } else {
            throw new IllegalArgumentException("Application not found or not authorized");
        }
    }
}
