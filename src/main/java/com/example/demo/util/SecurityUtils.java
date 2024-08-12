package com.example.demo.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.demo.service.AllService;

public class SecurityUtils {
	public static String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		 // OAuth2User인 경우
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();

            // Google의 고유 식별자를 얻으려면 "sub" 속성을 사용
            String googleUserId = principal.getAttribute("sub"); // Google의 고유 식별자
            if (googleUserId != null) {
                return googleUserId;
            }

            // Kakao의 고유 식별자를 얻으려면 "id" 속성을 사용
            String kakaoUserId = principal.getAttribute("id"); // Kakao의 고유 식별자
            if (kakaoUserId != null) {
                return kakaoUserId;
            }

            // 또는 사용자의 이메일 주소를 반환할 수 있습니다.
            return principal.getName(); // 일반적으로 이메일 주소가 될 수 있습니다.
        }

		// 인증된 사용자인 경우
		if (authentication != null && authentication.isAuthenticated()) {
			return authentication.getName(); // 기본 인증의 사용자 이름을 반환
		}

		// 인증되지 않은 경우
		return "Anonymous";
	}
}