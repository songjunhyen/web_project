package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.service.AdminService;
import com.example.demo.vo.Admin;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	//head에 클래스 
	@GetMapping("/admin/Signup")
	public String signUP() {
		return "user/signup";
	}

	@GetMapping("/admin/Login")
	public String Login() {
		return "user/login";
	}

	@GetMapping("/admin/Modify")
	public String Modify() {
		return "user/modify";
	}

	@GetMapping("/admin/Check")
	public String check(Admin admin, Model model) {
		model.addAttribute("admin",admin);
		return "admin/check";
	}

	@PostMapping("/admin/signup")
	public String signup(String userid, String pw, String name, String email) {
		Admin newAdmin = new Admin(userid, pw, name, email);
		adminService.signup(newAdmin);
		return "user/login";
	}

//유저 로그인에서 무관하게 사용하니 없어도 됨
//	@PostMapping("/admin/login")
//	public String login(HttpSession session, Model model, String userid, String pw) {		
//		 boolean idExists = adminService.checking(userid,pw);
//		    if (!idExists) {
//		        model.addAttribute("message", "아이디 혹은 비밀번호가 잘못되었습니다.");
//		        return "error"; // 사용자가 존재하지 않는 경우 에러 페이지로 리턴
//		    }else {
//		    	int memberid = adminService.getid(userid,pw); //서비스에 로그인 메서드 추가하고 로그인하면 Admin타입 반환하도록 변경
//		    	session.setAttribute("id", memberid);
//		    	session.setAttribute("viplevel", 0); // 반환된 Admin에 get 써서 그걸 저장하자.
//		    	session.setAttribute("class", 1); // 반환된 Admin에 get 써서 그걸 저장하자.
//		    }		    
//		return "product/main";
//	}

	@GetMapping("/admin/Logout")
	public String Logout(HttpSession session) { //세션사용하면 로그아웃 시 id나 userid 사용가능하니 받을 것도 없을 거고...
		// 세션에서 userid 제거
	    session.removeAttribute("id");
	    // 세션 무효화 
		session.invalidate();
		return "product/main";
	}

	@PostMapping("/admin/modify")
	public String modify(HttpSession session, String pw, String name, String email) {
		int id = (int) session.getAttribute("id");
		adminService.modify(id,pw, name, email);
		return "product/main";
	}

	@GetMapping("/admin/Signout") // jsp쪽에서 비번 체크하도록
	public String Sigbout(HttpSession session,int id) {
		adminService.Signout(id);
		// 세션에서 userid 제거
	    session.removeAttribute("id");
	    // 세션 무효화 
		session.invalidate();
		return "product/main";
	}
}
