package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.vo.Member;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = userDao.findByUserid(username);
        if (member == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // UserDetails 객체를 반환합니다. 비밀번호와 권한을 설정할 수 있습니다.
        return new User(member.getUserid(), member.getUserpw(), new ArrayList<>());
    }
}