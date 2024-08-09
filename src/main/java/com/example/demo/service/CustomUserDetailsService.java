package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.dao.AdminDao;
import com.example.demo.dao.UserDao;
import com.example.demo.vo.Admin;
import com.example.demo.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AdminDao adminDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 기존 사용자 정보 로드 로직
        Member member = userDao.findByUserid(username);
        if (member != null) {
            return new org.springframework.security.core.userdetails.User(member.getUserid(), member.getUserpw(), new ArrayList<>());
        }
        
        // Google 이메일로 사용자 검색
        Member googleUser = userDao.findByUserEmail(username);
        if (googleUser != null) {
            return new org.springframework.security.core.userdetails.User(googleUser.getUserid(), googleUser.getUserpw(), new ArrayList<>());
        }

        Admin admin = adminDao.findByUserid(username);
        if (admin != null) {
            return new org.springframework.security.core.userdetails.User(admin.getAdminId(), admin.getAdminPw(), new ArrayList<>());
        }

        throw new UsernameNotFoundException("User not found");
    }

    public UserDetails loadUserByGoogle(String email) throws UsernameNotFoundException {
        // 이메일 기반으로 사용자 검색
        Member member = userDao.findByUserEmail(email);
        if (member != null) {
            return new org.springframework.security.core.userdetails.User(member.getUserid(), member.getUserpw(), new ArrayList<>());
        }

        throw new UsernameNotFoundException("User not found");
    }
    
    
}	