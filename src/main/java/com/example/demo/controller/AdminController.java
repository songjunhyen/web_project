package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@GetMapping("/admin/Signupreport")
	public String signUPre() {
		return "admin/signupreport";
	}

	@GetMapping("/admin/Modify")
	public String Modify() {
		return "admin/modify";
	}

	@GetMapping("/admin/Modify1")
	public String Modif1() {
		return "admin/Adminmodify";
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
	public String signup(@RequestParam String name, @RequestParam String email, @RequestParam int adminclass, RedirectAttributes redirectAttributes) {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String formattedDateTime = currentTime.format(formatter);
		String adminid = "admin" + formattedDateTime;

		UUID uuid = UUID.randomUUID();
		String adminpw = uuid.toString().replace("-", "");

		Admin newAdmin = new Admin(adminid, adminpw, name, email);
		adminService.signup(newAdmin);
		redirectAttributes.addFlashAttribute("admin", newAdmin);
		return "redirect:/admin/Signupreport";
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
		
	@RequestMapping("/admin/checkEmail.do")
	@ResponseBody
	public Map<Object, Object> checkEmail(@RequestParam String email) {
		int id = adminService.getid3(email);
        //getMemberId는 id로 멤버의 dto를 꺼내오는 메소드
        
		Map<Object, Object> map = new HashMap<>();

		// 아이디가 존재하지 않으면
		if(id == 0) {
			map.put("cnt", 0);
		// 아이디가 존재하면
		}else {
			map.put("cnt", 1);
		}
		
		return map;
	}
	
	@RequestMapping("/admin/checkName.do")
	@ResponseBody
	public Map<Object, Object> checkName(@RequestParam String name) {
		int id = adminService.getid4(name);
        //getMemberId는 id로 멤버의 dto를 꺼내오는 메소드
        
		Map<Object, Object> map = new HashMap<>();

		// 아이디가 존재하지 않으면
		if(id == 0) {
			map.put("cnt", 0);
		// 아이디가 존재하면
		}else {
			Admin foundadmin = adminService.getbyname(name);
			map.put("cnt", 1);
		}
		
		return map;
	}
	
	@RequestMapping(value = "/admin/Searching", method = RequestMethod.POST)
	@ResponseBody
	public List<Admin> searchAdmins(@RequestParam(required = false) String adminclass,
			@RequestParam(required = false) String name, @RequestParam(required = false) String email) {
		// 빈값 처리
		if (adminclass == null || adminclass.isEmpty()) {
			adminclass = null; // adminclass가 빈 문자열일 경우 null로 처리
		}
		if (name == null || name.isEmpty()) {
			name = null; // name이 빈 문자열일 경우 null로 처리
		}
		if (email == null || email.isEmpty()) {
			email = null; // email이 빈 문자열일 경우 null로 처리
		}

		// 검색 로직을 구현합니다.
		List<Admin> admins = adminService.searchAdmin(adminclass, name, email);
		return admins;
	}
	
	 @PostMapping("/admin/resetAD")
	    public String resetPassword(@RequestParam String adminid, Model model) {
	        // 초기화
		 	LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
			String formattedDateTime = currentTime.format(formatter);
			String newId = "admin" + formattedDateTime;
	        String newPassword = UUID.randomUUID().toString().replace("-", "");
	        
	        adminService.resetPassword(adminid, newId, newPassword);
	        model.addAttribute("message", "비밀번호가 초기화되었습니다.");
	        return "admin/modify";
	    }
}
