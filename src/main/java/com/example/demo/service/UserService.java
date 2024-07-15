package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.vo.Member;

@Service
public class UserService {
	private UserDao userDao;

	UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	public void signup(Member member) {
		userDao.signup(member);
	}

	public void modify(int id, String pw, String name, String email, String address) {
		userDao.modify(id, pw, name, email, address);
	}

	public void Signout(int id) {
		userDao.signout(id);
	}

	public boolean checking(String userid, String pw) {
		boolean isin = true;
		// id로 체크
		if (userDao.checkid(userid)== 0) {
			isin = false;
		} else {
			if (userDao.checkpw(userid,pw)==0) {
				isin = false;
			}
		}
		return isin;
	}

	public int getid(String userid, String pw) {		
		return userDao.getid(userid,pw);
	}
}
