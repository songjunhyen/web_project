package com.example.demo.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleAuthService {

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;

	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final HttpTransport HTTP_TRANSPORT;

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException("Failed to initialize HTTP transport", e);
		}
	}

	public GoogleAuthService() {
	}

	public GoogleIdToken.Payload verifyToken(String idTokenString) throws GeneralSecurityException, IOException {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
				.setAudience(Collections.singletonList(clientId)).build();

		GoogleIdToken idToken = verifier.verify(idTokenString);
		if (idToken != null) {
			return idToken.getPayload();
		} else {
			throw new SecurityException("Invalid ID token");
		}
	}

	public boolean validateToken(String token) {
		try {
			GoogleIdToken.Payload payload = verifyToken(token);
			return payload != null;
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getUsernameFromToken(String token) {
		try {
			GoogleIdToken.Payload payload = verifyToken(token);
			if (payload != null) {
				return payload.getEmail(); // 사용자 이메일 또는 ID를 반환
			}
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
}