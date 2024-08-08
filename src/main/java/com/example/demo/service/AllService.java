package com.example.demo.service;

import com.example.demo.dao.AdminDao;
import com.example.demo.dao.UserDao;
import com.example.demo.vo.Member;

import org.springframework.stereotype.Service;

@Service
public class AllService {
    private final UserDao userDao;
    private final AdminDao adminDao;
    
    AllService(UserDao userDao,AdminDao adminDao){
    	 this.userDao = userDao;
    	 this.adminDao = adminDao;
    }
    
    public String isuser(String userid) {
		int userCount = userDao.countByUserid(userid);
		boolean adminCount = adminDao.checkid(userid);

		if (userCount > 0) {
			return "user";
		} else if (userCount == 0) {
			if (adminCount) {
				return "admin"; // 또는 적절한 예외 처리를 고려할 수 있습니다.
			} else {
				// 예상 외의 경우에 대한 처리
				return "unknown"; // or throw an exception if this should be an error
			}
		} else {
			// 예상 외의 경우에 대한 처리
			return "unknown"; // or throw an exception if this should be an error
		}
	}
	
	public int getadminclass(String userid) {		
		return adminDao.getadminclass(userid);
	}
	
    public Member saveOrUpdateUser(String email, String name) {
        // DB에서 사용자 검색 또는 새로 생성
        Member member = userDao.findByUserid(email);
        if (member == null) {
            member = new Member();
            member.setUserid(email);
            member.setEmail(email);
            member.setName(name);
            // 추가적인 사용자 속성 설정
        } else {
            // 필요한 경우 사용자 정보 업데이트
            member.setName(name);
        }
        // 사용자 정보를 DB에 저장
        userDao.save(member);
        return member;
    }
}
