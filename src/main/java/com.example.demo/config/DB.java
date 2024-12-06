package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DB {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        try {
            // MySQL 데이터베이스 연결 정보 설정
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/saramin_data"); // 데이터베이스 이름을 설정
            dataSource.setUsername("ubuntu"); // MySQL 사용자 이름
            dataSource.setPassword("jyoun0320!@#"); // MySQL 비밀번호

            System.out.println("MySQL 연결 성공");
        } catch (Exception e) {
            System.err.println("MySQL 연결 실패: " + e.getMessage());
            throw new RuntimeException(e); // 연결 실패 시 예외 던지기
        }

        return dataSource;
    }
}
