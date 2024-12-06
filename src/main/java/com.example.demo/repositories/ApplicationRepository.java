package com.example.demo.repositories;

import com.example.demo.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUserEmail(String email);

    Optional<Application> findByIdAndUserEmail(Long id, String email);
}
