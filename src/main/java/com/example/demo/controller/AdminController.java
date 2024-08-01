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
import com.example.demo.util.SecurityUtils;
import com.example.demo.vo.Admin;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping("/admin/Dashboard")
	public String Board(Model model) {
		String userid = SecurityUtils.getCurrentUserId();
		Admin foundadmin = adminService.getbyuserid(userid);
		model.addAttribute("admin", foundadmin);
		return "admin/dashboard";
	}

	@GetMapping("/admin/Signup")
	public String signUP() {
		return "admin/signup";
	}

	@GetMapping("/admin/Modify")
	public String Modify() {
		return "admin/modify";
	}
	
	@GetMapping("/admin/Search")
	public String Search() {
		return "admin/search";
	}

	@GetMapping("/admin/searchbyemail")
	public String check(String email, Model model) {
		Admin foundadmin = adminService.getbyemail(email);
		model.addAttribute("admin", foundadmin);
		return "admin/check";
	}

	@PostMapping("/admin/signup")
	public String signup(@RequestParam String name, @RequestParam String email, @RequestParam int adminclass) {
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

	@PostMapping("/admin/modify")
	public String modify(HttpSession session, @RequestParam String name, @RequestParam String email) {

		UUID uuid = UUID.randomUUID();
		String newpw = uuid.toString().replace("-", "");

		adminService.modify(newpw, name, email);
		return "product/main";
	}

	@GetMapping("/admin/Signout") // jsp쪽에서 비번 체크하도록
	public String Sigbout(HttpSession session, @RequestParam String email) {
		int adminid = (int) session.getAttribute("id");
		adminService.signout(adminid, email);

		return "redirect:/Home/Main";
	}

	@GetMapping("/admin/logout") // jsp쪽에서 비번 체크하도록
	public String logout(HttpSession session) {
		// 세션에서 userid 제거
		session.removeAttribute("id");
		// 세션 무효화
		session.invalidate();

		return "redirect:/Home/Main";
	}
}
