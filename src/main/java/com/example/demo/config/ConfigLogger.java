package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigLogger implements ApplicationRunner {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Google Client ID: " + clientId);
    }
}	