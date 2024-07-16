package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.AdminService;
import com.example.demo.vo.Admin;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	// head에 클래스
	@GetMapping("/admin/Signup")
	public String signUP() {
		return "admin/signup";
	}

	@GetMapping("/admin/Login")
	public String Login() {
		return "admin/login";
	}

	@GetMapping("/admin/Modify")
	public String Modify() {
		return "admin/modify";
	}

	@GetMapping("/admin/searchbyemail")
	public String check(String email, Model model) {
		Admin foundadmin = adminService.getbyemail(email);
		model.addAttribute("admin", foundadmin);
		return "admin/check";
	}

	@PostMapping("/admin/signup")
	public String signup(@RequestParam String name,@RequestParam  String email,@RequestParam  int adminclass) {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String formattedDateTime = currentTime.format(formatter);
		String adminid = "admin" + formattedDateTime;

		UUID uuid = UUID.randomUUID();
		String adminpw = uuid.toString().replace("-", "");

		Admin newAdmin = new Admin(adminid, adminpw, name, email);
		adminService.signup(newAdmin);
		return "admin/singupreport"; // 이 jsp파일에서 가입시 생성한 id, pw 보여주고 그걸로 사용
	}

	@PostMapping("/admin/login")
	public String login(HttpSession session, Model model,@RequestParam  String userid,@RequestParam  String pw) {
		boolean idExists = adminService.checking(userid, pw);
		if (!idExists) {
			model.addAttribute("message", "아이디 혹은 비밀번호가 잘못되었습니다.");
			return "error"; // 사용자가 존재하지 않는 경우 에러 페이지로 리턴
		} else {
			int adminid = adminService.getid(userid, pw); // 서비스에 로그인 메서드 추가하고 로그인하면 Admin타입 반환하도록 변경
			Admin foundadmin = adminService.getadmin(adminid, userid, pw);
			session.setAttribute("id", adminid);
			session.setAttribute("adminclass", foundadmin.getAdminclass()); // 반환된 Admin에 get 써서 그걸 저장하자.
		}
		return "product/main";
	}

	@GetMapping("/admin/Logout")
	public String Logout(HttpSession session) { // 세션사용하면 로그아웃 시 id나 userid 사용가능하니 받을 것도 없을 거고...
		// 세션에서 userid 제거
		session.removeAttribute("id");
		// 세션 무효화
		session.invalidate();
		return "product/main";
	}

	@PostMapping("/admin/modify")
	public String modify(HttpSession session,@RequestParam  String name,@RequestParam  String email) {
		int id = (int) session.getAttribute("id");
		adminService.modify(id,name, email);
		return "product/main";
	}

	@GetMapping("/admin/Signout") // jsp쪽에서 비번 체크하도록
	public String Sigbout(HttpSession session,@RequestParam int email) {
		int adminid = (int) session.getAttribute("id");
		adminService.Signout(adminid,email);
		// 세션에서 userid 제거
		session.removeAttribute("id");
		// 세션 무효화
		session.invalidate();
		return "product/main";
	}
}
