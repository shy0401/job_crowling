package com.example.demo.services;

import com.example.demo.models.Resume;
import com.example.demo.models.User;
import com.example.demo.repositories.ResumeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    /**
     * 새로운 지원서 생성
     *
     * @param user    지원서를 작성하는 사용자
     * @param title   지원서 제목
     * @param content 지원서 내용
     * @return 저장된 Resume 객체
     */
    public Resume createResume(User user, String title, String content) {
        Resume resume = new Resume(user, title, content);
        return resumeRepository.save(resume);
    }

    /**
     * 특정 사용자와 연관된 모든 지원서 조회
     *
     * @param user 사용자 객체
     * @return 해당 사용자의 지원서 목록
     */
    public List<Resume> getResumesByUser(User user) {
        return resumeRepository.findByUser(user);
    }

    /**
     * 특정 ID의 지원서 조회
     *
     * @param id 지원서 ID
     * @return Optional로 감싸진 Resume 객체
     */
    public Optional<Resume> getResumeById(Long id) {
        return resumeRepository.findById(id);
    }

    /**
     * 지원서 삭제
     *
     * @param resume 삭제할 Resume 객체
     */
    public void deleteResume(Resume resume) {
        resumeRepository.delete(resume);
    }
}
