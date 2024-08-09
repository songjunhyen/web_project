package com.example.demo.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.vo.Member;

@Service
public class UserService {
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	public void signup(Member member) {
		String encodedPassword = passwordEncoder.encode(member.getUserpw());
		member.setUserpw(encodedPassword);
		userDao.signup(member);
	}

	public void modify(String userid, String pw, String name, String email, String address) {
		String encodedPassword = passwordEncoder.encode(pw);
		userDao.modify(userid, encodedPassword, name, email, address);
	}

	public void signout(int id) {
		userDao.signout(id);
	}

	public boolean checking(String userid, String pw) {
		Member member = userDao.findByUserid(userid);
		if (member != null) {
			// 입력된 비밀번호와 저장된 암호화된 비밀번호를 비교
			String storedPassword = member.getUserpw();
			boolean matches = passwordEncoder.matches(pw, storedPassword);

			// 디버깅 로그
			System.out.println("Stored password: " + storedPassword);
			System.out.println("Entered password: " + pw);
			System.out.println("Password matches: " + matches);

			return matches;
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

	public boolean existsByUserid(String userid) {
		return userDao.countByUserid(userid) > 0;
	}

	public int getid2(String userid) {
		Member usr = userDao.findByUserid(userid);
		if(usr == null) {
			return 0;
		}
		return usr.getId();
	}

	public String isuser(String userid) {
		if (userDao.countByUserid(userid) > 0) {
			return "user";
		} else {
			return "admin";
		}
	}

	public boolean checkon(String userid, String pw) {
		Member member = userDao.findByUserid(userid);
        if (member != null) {
            return passwordEncoder.matches(pw, member.getUserpw());
        }
        return false;
	}

	public int getid3(String email) {
		Member usr = userDao.findByUserEmail(email);
		if(usr == null) {
			return 0;
		}
		return usr.getId();
	}

	public List<Member> searchUser(String name, String email) {		
		return userDao.searcUL(name, email);
	}

	public void resetPassword(String userid, String newPassword) {
		 // 새 비밀번호를 암호화합니다.
	    String encodedPassword = passwordEncoder.encode(newPassword);

	    // 암호화된 비밀번호를 데이터베이스에 저장합니다.
	    userDao.resetPassword(userid, encodedPassword);
    }

}