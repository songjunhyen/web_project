package com.example.demo.config;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.service.AllService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final AllService allService; // AllService를 주입받을 필드

    // 생성자 주입
    public CustomOAuth2UserService(AllService allService) {
        this.allService = allService;
    }
	
	@Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        String accessToken = userRequest.getAccessToken().getTokenValue();       
        // Google 사용자 정보를 가져오는 API 호출
        String userInfoEndpointUri = "https://www.googleapis.com/oauth2/v3/userinfo";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> userAttributes = restTemplate.getForObject(userInfoEndpointUri + "?access_token=" + accessToken, Map.class);
                
        // 사용자 정보를 기반으로 OAuth2User 생성
        if (userAttributes != null) {
        	String email = (String) userAttributes.get("email");
        	String name = (String) userAttributes.get("sub");
        	ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (sra != null) {
                HttpServletRequest request = sra.getRequest();
                HttpSession session = request.getSession();
                session.setAttribute("customuser", email);
                session.setAttribute("userRole", "user"); // 예시로 사용자 역할을 저장
                
                allService.saveOrUpdateUser(email, name);
                // 필요에 따라 다른 사용자 정보도 저장 가능
            }
            return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userAttributes,
                "sub"
            );

        } else {
            throw new RuntimeException("Failed to retrieve user information from Google");
        }
    }
}		