package com.example.demo.goolgle;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.AllService;
import com.example.demo.service.GoogleAuthService;
import com.example.demo.vo.Member;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Value;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Value("${spring.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Autowired
    private HttpSession httpSession;
    
    @Autowired
    private GoogleAuthService googleAuthService;

    @Autowired
    private AllService allService;

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @PostMapping("/google")
    public ResponseEntity<?> handleGoogleLogin(@RequestBody Map<String, String> request) {
        String idTokenString = request.get("idToken");

        try {
            GoogleIdToken.Payload payload = googleAuthService.verifyToken(idTokenString);
            String userEmail = payload.getEmail();
            String name = (String) payload.get("name");

            // 사용자 인증 및 세션 처리
            Member user = allService.saveOrUpdateUser(userEmail, name);
            httpSession.setAttribute("user", user);
            httpSession.setAttribute("userRole2", "user");

            return ResponseEntity.ok(Map.of("redirectUrl", "/Home/Main"));
        } catch (Exception e) {
            logger.error("Error during Google token verification", e);
            return ResponseEntity.status(500).body(Map.of("error", "Server error during token verification"));
        }
    }


    @GetMapping("/google/login")
    public String googleLogin() {
        return "AllLogin"; // 구글 로그인 페이지를 반환
    }
}

