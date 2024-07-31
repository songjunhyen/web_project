package com.example.demo.service;

import com.example.demo.dao.AdminDao;
import com.example.demo.dao.UserDao;
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
    
}
