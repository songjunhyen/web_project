package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.AdminDao;
import com.example.demo.vo.Admin;

@Service
public class AdminService {
    private final AdminDao adminDao;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminDao adminDao, PasswordEncoder passwordEncoder) {
        this.adminDao = adminDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(Admin newAdmin) {
        String encodedPassword = passwordEncoder.encode(newAdmin.getAdminPw());
        newAdmin.setAdminPw(encodedPassword);
        adminDao.signup(newAdmin);
    }

    public boolean checking(String userid, String pw) {
        if (!adminDao.checkid(userid)) {
            return false;
        } else {
            String hashedPassword = adminDao.getHashedPassword(userid);

            // 실제로 암호화된 비밀번호를 출력하지 말고, 디버깅이 필요하다면 다른 방법 사용
            System.out.println("Stored hashed password: " + hashedPassword);
            System.out.println("Entered password: " + pw);

            return passwordEncoder.matches(pw, hashedPassword);
        }
    }

    public int getid(String userid) {
        return adminDao.getid(userid);
    }

    public void modify(String newpw, String name, String email) {
        String encodedPassword = passwordEncoder.encode(newpw);
        adminDao.modify(encodedPassword, name, email);
    }

    public void signout(int id, String email) {
        adminDao.signout(id, email);
    }

    public Admin getadmin(int adminid, String userid) {
        return adminDao.getadmin(adminid, userid);
    }

    public Admin getbyemail(String email) {
        return adminDao.getbyemail(email);
    }

    public int getadminclass(String username) {
        return adminDao.getadminclass(username);
    }

	public Admin getbyuserid(String userid) {
		return adminDao.getbyuserid(userid);
	}

	public int getid2(String userid) {
		Admin usr = adminDao.findByUserid(userid);
		if(usr == null) {
			return 0;
		}
		return usr.getId();
	}
	
	public int getid3(String email) {
		Admin usr = adminDao.getbyemail(email);
		if(usr == null) {
			return 0;
		}
		return usr.getId();
	}
}