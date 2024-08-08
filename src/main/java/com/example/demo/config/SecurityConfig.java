package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	@Value("${session.expired.redirect.url:/Home/login}")
	private String redirectUrl;

	@Bean
	public CustomSessionExpiredStrategy customSessionExpiredStrategy(
			@Value("${session.expired.redirect.url:/Home/login}") String redirectUrl) {
		return new CustomSessionExpiredStrategy(redirectUrl);
	}
	   
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        	.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/Home/login").permitAll()
                .requestMatchers("/Home/Main").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/Home/login")
                .loginProcessingUrl("/Home/Login")
                .usernameParameter("userid")
                .passwordParameter("pw")
                .permitAll()
            )
            .logout(logout -> logout
            	.logoutRequestMatcher(new AntPathRequestMatcher("/Home/logout", "POST"))
                .logoutSuccessUrl("/Home/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "XSRF-TOKEN","AUTH-TOKEN")
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/Home/login")
                    .userInfoEndpoint(userInfo -> userInfo
                            .userService(customOAuth2UserService)
                        )
                    .defaultSuccessUrl("/Home/Main")
                    .failureUrl("/Home/login?error=true")
                )
            .httpBasic(httpBasic -> httpBasic.disable())
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(1)
                    .expiredSessionStrategy(customSessionExpiredStrategy(redirectUrl))
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder를 빈으로 등록
    }

}