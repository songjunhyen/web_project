package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 비활성화
            .authorizeHttpRequests(authorize -> authorize
            		  .requestMatchers("/Home/Main", "/admin/Login", "/user/Login", "/user/Signup").permitAll()
                .anyRequest().permitAll()  // 모든 요청 허용
            )
            .formLogin(formLogin -> formLogin
                .disable()  // 기본 로그인 페이지 비활성화
            )
            .httpBasic().disable();  // HTTP 기본 인증 비활성화

        return http.build();
    }
}