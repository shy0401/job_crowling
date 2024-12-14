package com.example.demo.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false)
    private String status;  // 지원 상태 (예: Pending, Cancelled)

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date appliedAt;  // 지원 날짜

    @Column(columnDefinition = "TEXT")
    private String resume;  // 이력서 내용

    // 기본 생성자
    public Application() {
        this.status = "Pending"; // 기본 상태
        this.appliedAt = new Date(); // 기본 지원 날짜
    }

    // 사용자 정의 생성자
    public Application(User user, Job job, String resume) {
        this.user = user;
        this.job = job;
        this.resume = resume;
        this.status = "Pending";
        this.appliedAt = new Date();
    }

    public Application(User user, Job job, String resume, String status) {
        this.user = user;
        this.job = job;
        this.resume = resume;
        this.status = status;
        this.appliedAt = new Date();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Date appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
