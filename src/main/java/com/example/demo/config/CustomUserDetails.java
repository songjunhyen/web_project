package com.example.demo.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.demo.vo.Member;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 예를 들어, 사용자의 권한을 반환할 수 있습니다.
        return null; // 기본적인 예제에서는 권한을 반환하지 않음
    }

    @Override
    public String getPassword() {
        return member.getUserpw(); // 사용자의 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return member.getUserid(); // 사용자의 아이디 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }

    public Member getMember() {
        return member; // Member 객체 반환
    }
}