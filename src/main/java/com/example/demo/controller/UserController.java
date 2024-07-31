package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.form.SignUpForm;
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
	public String signUP(Model model) {
		model.addAttribute("SignUpForm", new SignUpForm());
		return "user/signup";
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
	public String signup(Model model, SignUpForm signupform) {
		String userid = signupform.getUserid();
		String pw = signupform.getPw();
		String name = signupform.getName();
		String email = signupform.getEmail();
		String address = signupform.getAddress();

		String isclass = userService.isuser(userid);
		if (isclass.equals("user")) {
			model.addAttribute("errorMessage", "이미 존재하는 사용자입니다.");
			return "redirect:/user/Signup";
		}

		Member newMember = new Member(userid, pw, name, email, address);
		userService.signup(newMember);
		return "redirect:/Home/Main";
	}
	
	@PostMapping("/user/modify")
	public String modify(HttpSession session, String pw, String name, String email, String address) {
		int id = (int) session.getAttribute("id");
		userService.modify(id, pw, name, email, address);
		return "redirect:/Home/Main";
	}

	@GetMapping("/user/Signout") // jsp쪽에서 비번 체크하도록
	public String Sigbout(HttpSession session, int id) {
		userService.signout(id);
		// 세션에서 userid 제거
		session.removeAttribute("id");
		session.removeAttribute("islogined");
		// 세션 무효화
		session.invalidate();
		return "redirect:/Home/Main";
	}

}
