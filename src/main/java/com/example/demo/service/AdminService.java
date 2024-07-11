package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dao.AdminDao;
import com.example.demo.vo.Admin;

@Service
public class AdminService {
	private AdminDao adminDao;
	
	AdminService(AdminDao adminDao){
		this.adminDao = adminDao;
	}

	public void signup(Admin newAdmin) {
		adminDao.signup(newAdmin);
	}

	public boolean checking(String userid, String pw) {
		boolean isin = true;
		// id로 체크
		if (!adminDao.checkid(userid)) {
			isin = false;
		} else {
			if (!adminDao.checkpw(userid,pw)) {
				isin = false;
			}
		}
		return isin;
	}

	public int getid(String userid, String pw) {
		return adminDao.getid(userid,pw);
	}

	public void modify(int id, String pw, String name, String email) {
		adminDao.modify(id, pw, name, email);
	}

	public void Signout(int id) {
		adminDao.signout(id);
	}
}
