package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.example.demo.Handler.AdminAuthenticationSuccessHandler;
import com.example.demo.Handler.CustomAuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // 쿠키에 CSRF 토큰 저장
            )
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/user/Login")  // 커스텀 로그인 페이지 지정
                .loginProcessingUrl("/user/login")  // 로그인 처리 URL
                .usernameParameter("userid")  // 사용자명 파라미터 이름
                .passwordParameter("pw")  // 비밀번호 파라미터 이름
                .successHandler(authenticationSuccessHandler()) // 로그인 성공 핸들러 설정
                .permitAll()  // 로그인 페이지는 인증 없이 접근 가능
            )
            .formLogin(formLogin -> formLogin
                    .loginPage("/admin/Login")  // 커스텀 로그인 페이지 지정
                    .loginProcessingUrl("/admin/login")  // 로그인 처리 URL
                    .usernameParameter("userid")  // 사용자명 파라미터 이름
                    .passwordParameter("pw")  // 비밀번호 파라미터 이름
                    .successHandler(adminAuthenticationSuccessHandler()) // 로그인 성공 핸들러 설정
                    .permitAll()  // 로그인 페이지는 인증 없이 접근 가능
                )
            .logout(logout -> logout
                .logoutUrl("/logout")  // 로그아웃 URL
                .logoutSuccessUrl("/user/Login?logout")  // 로그아웃 성공 시 리디렉션 URL
                .permitAll()  // 로그아웃 페이지는 인증 없이 접근 가능
            )
            .httpBasic(httpBasic -> httpBasic.disable()) // HTTP 기본 인증 비활성화
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션 생성 정책 설정
                .maximumSessions(1)  // 최대 세션 수 제한
                .expiredUrl("/user/login")  // 세션 만료 시 리디렉션 URL
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
    
    @Bean
    public AdminAuthenticationSuccessHandler adminAuthenticationSuccessHandler() {
        return new AdminAuthenticationSuccessHandler();
    }
}