package com.example.demo.config;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomSessionExpiredStrategy implements SessionInformationExpiredStrategy {

    private final String redirectUrl;

    public CustomSessionExpiredStrategy() {
        // 기본 생성자
        this.redirectUrl = "/Home/login"; // 기본 URL
    }

    public CustomSessionExpiredStrategy(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletRequest request = event.getRequest();
        HttpServletResponse response = event.getResponse();

        response.sendRedirect(redirectUrl);
    }
}