package com.example.demo.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoAuthService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    public Map<String, Object> verifyToken(String accessToken) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            KAKAO_USER_INFO_URL,
            HttpMethod.GET,
            entity,
            Map.class
        );

        return response.getBody();
    }
    
    public String getKakaoUserId(String accessToken) {
        try {
            Map<String, Object> userInfo = verifyToken(accessToken);
            // 카카오의 사용자 ID는 "id" 속성으로 제공됨
            if (userInfo != null && userInfo.containsKey("id")) {
                return userInfo.get("id").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getKakaoEmail(String accessToken) {
        try {
            Map<String, Object> userInfo = verifyToken(accessToken);
            // 카카오의 이메일은 "kakao_account.email" 속성으로 제공됨
            if (userInfo != null && userInfo.containsKey("kakao_account")) {
                Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
                if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
                    return kakaoAccount.get("email").toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}	