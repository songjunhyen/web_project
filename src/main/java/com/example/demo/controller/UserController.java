package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.service.UserService;
import com.example.demo.vo.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/test/user/Main")
	public String mainPage() {
		return "user/main";
	}

	@GetMapping("/user/Signup")
	public String signUP() {
		return "user/signup";
	}

	@GetMapping("/user/Login")
	public String Login() {
		return "user/login";
	}

	@GetMapping("/user/Modify")
	public String Modify() {
		return "user/modify";
	}

	@GetMapping("/user/Check")
	public String check(Member member, Model model) {
		model.addAttribute("member", member);
		return "user/check";
	}

	@PostMapping("/user/signup")
	public String signup(String userid, String pw, String name, String email, String address) {
		Member newMember = new Member(userid, pw, name, email, address);
		userService.signup(newMember);
		return "user/login";
	}

	@PostMapping("/user/login")
	public String login(HttpSession session, Model model, String userid, String pw) {		
		 boolean idExists = userService.checking(userid,pw);
		    if (!idExists) {
		        model.addAttribute("message", "아이디 혹은 비밀번호가 잘못되었습니다.");
		        return "error"; // 사용자가 존재하지 않는 경우 에러 페이지로 리턴
		    }else {
		    	int memberid = userService.getid(userid,pw); //서비스에 로그인 메서드 추가하고 로그인하면 Member타입 반환하도록 변경
		    	session.setAttribute("id", memberid);
		    	session.setAttribute("userid", userid);
		    	session.setAttribute("class", 0); // 등급
		    }		    
		return "product/main";
	}

	@GetMapping("/user/Logout")
	public String Logout(HttpSession session) { //세션사용하면 로그아웃 시 id나 userid 사용가능하니 받을 것도 없을 거고...
		// 세션에서 id 제거
	    session.removeAttribute("id");
	    session.removeAttribute("userid");
	    session.removeAttribute("class");
	    // 세션 무효화 
		session.invalidate();
		return "product/main";
	}

	@PostMapping("/user/modify")
	public String modify(HttpSession session, String pw, String name, String email, String address) {
		int id = (int) session.getAttribute("id");
		userService.modify(id,pw, name, email, address);
		return "product/main";
	}

	@GetMapping("/user/Signout") // jsp쪽에서 비번 체크하도록
	public String Sigbout(HttpSession session,int id) {
		userService.Signout(id);
		// 세션에서 userid 제거
	    session.removeAttribute("id");
	    // 세션 무효화 
		session.invalidate();
		return "product/main";
	}

}
