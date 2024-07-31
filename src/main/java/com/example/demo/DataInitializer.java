	package com.example.demo;
	
	import org.springframework.boot.CommandLineRunner;
	import org.springframework.stereotype.Component;
	
	import com.example.demo.service.AdminService;
	import com.example.demo.service.UserService;
	import com.example.demo.vo.Admin;
	import com.example.demo.vo.Member;
	
	@Component
	public class DataInitializer implements CommandLineRunner {
	    private final UserService userService;
	    private final AdminService adminService;
	
	    public DataInitializer(UserService userService, AdminService adminService) {
	    	this.userService =  userService;
	        this.adminService = adminService;
	    }
	
	    @Override
	    public void run(String... args) throws Exception {
	    	 // 관리자가 이미 등록되어 있는지 확인
	        if (adminService.getbyemail("admin@example.com") == null) {
	            // 관리자 객체 생성
	            Admin admin = new Admin("admin", "adminpw", "Admin", "admin@example.com");
	            // 관리자 등록
	            adminService.signup(admin);
	        }

	        // 사용자가 이미 등록되어 있는지 확인
	        if (!userService.existsByUserid("12")) {
	            Member newMember = new Member("12", "12", "12", "0", "0");
	            userService.signup(newMember);
	        }	        	       
	    }	    	    
	}