package com.example.demo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LoginHistory")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // User 테이블의 ID와 연관
    private User user;

    @Column(name = "login_at", nullable = false)
    private LocalDateTime loginAt;

    @Column(name = "ip")
    private String ip;

    @Column(name = "device")
    private String device;

    // 기본 생성자
    public LoginHistory() {}

    // 매개변수 생성자
    public LoginHistory(User user, LocalDateTime loginAt, String ip, String device) {
        this.user = user;
        this.loginAt = loginAt;
        this.ip = ip;
        this.device = device;
    }

    public LoginHistory(Long id, String s, String unknownDevice) {
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(LocalDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
