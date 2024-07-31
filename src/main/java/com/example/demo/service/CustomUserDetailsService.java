package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 관리자 검색
		Admin admin = adminDao.findByUserid(username);
		if (admin != null) {
			// 관리자 정보를 반환
			return new User(admin.getAdminId(), admin.getAdminPw(), new ArrayList<>());
		}

		// 사용자 검색
		Member member = userDao.findByUserid(username);
		if (member != null) {
			// 사용자 정보를 반환
			return new User(member.getUserid(), member.getUserpw(), new ArrayList<>());
		}

		throw new UsernameNotFoundException("User not found");
	}	
	
}