package com.example.demo.Handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.demo.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import java.io.IOException;

public class AdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private AdminService adminService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String username = authentication.getName();

        // 관리자 세션에 로그인 정보 설정
        session.setAttribute("islogined", 1);
        session.setAttribute("userid", username);
        session.setAttribute("class", "admin"); // 관리자 권한 설정

		int adminclass = adminService.getadminclass(username);
		session.setAttribute("adminclass", adminclass); // 반환된 Admin에 get 써서 저장.
		
		
		// 관리자 리디렉션 URL 설정
        response.sendRedirect("/Home/Main");
    }
}