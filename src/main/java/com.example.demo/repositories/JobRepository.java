package com.example.demo.repositories;

import com.example.demo.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    /**
     * 모든 Job 데이터를 조회합니다.
     * JpaRepository의 기본 메서드 findAll()을 오버라이드하여 명시적으로 작성합니다.
     */
    @Override
    List<Job> findAll();

    /**
     * 특정 회사 이름으로 채용 정보를 검색합니다.
     * @param company 회사 이름
     * @return 회사 이름에 해당하는 Job 목록
     */
    List<Job> findByCompany(String company);

    /**
     * MySQL에서 높은 급여 순으로 채용 정보를 가져옵니다.
     * @return 급여 순으로 정렬된 Job 목록
     */
    @Query("SELECT j FROM Job j ORDER BY j.salary DESC")
    List<Job> findJobsBySalaryDescending();
}
