package com.example.demo.services;

import com.example.demo.models.Application;
import com.example.demo.models.User;
import com.example.demo.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> getApplications(User user) {
        return applicationRepository.findByUser(user);
    }

    public void applyForJob(Application application, User user) {
        application.setUser(user);
        applicationRepository.save(application);
    }
}
