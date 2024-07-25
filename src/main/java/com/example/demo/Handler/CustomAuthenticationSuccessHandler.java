package com.example.demo.Handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String username = authentication.getName();

        // 세션에 로그인 정보 설정
        session.setAttribute("islogined", 1);
        session.setAttribute("userid", username);
        session.setAttribute("class", "user"); // 사용자 권한 설정

        // 리디렉션할 URL 설정
        response.sendRedirect("/Home/Main");
    }
}