package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.vo.Member;


@Service
public class UserService {
	private UserDao userDao;
	private final PasswordEncoder passwordEncoder;

	UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	public void signup(Member member) {
		String encodedPassword = passwordEncoder.encode(member.getUserpw());
		member.setUserpw(encodedPassword);
		userDao.signup(member);
	}

	public void modify(int id, String pw, String name, String email, String address) {
		userDao.modify(id, pw, name, email, address);
	}

	public void Signout(int id) {
		userDao.signout(id);
	}

	public boolean checking(String userid, String pw) {
		Member member = userDao.findByUserid(userid);
		if (member != null && passwordEncoder.matches(pw, member.getUserpw())) {
			return true;
		}
		return false;
	}

	public int getid(String userid, String pw) {
		Member member = userDao.findByUserid(userid);
		if (member != null && passwordEncoder.matches(pw, member.getUserpw())) {
			return member.getId(); // Return the ID if credentials are correct
		}
		return -1; // Return -1 if user is not found or credentials are incorrect
	}	
	
}
