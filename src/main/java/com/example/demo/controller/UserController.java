package com.example.demo.controller;

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

import com.example.demo.form.SignUpForm;
import com.example.demo.service.UserService;
import com.example.demo.util.SecurityUtils;
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
	@GetMapping("/user/Search")
	public String Search() {
		return "user/usersearch";
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

	@PostMapping("/user/Checking")
	public String checking(@RequestParam String pw, Model model) {
		String userid = SecurityUtils.getCurrentUserId();
		boolean result = userService.checkon(userid, pw);
		String resulted = result ? "true" : "false"; // 간단한 삼항 연산자 사용
		model.addAttribute("result", resulted);

		System.out.println("Resulted value in controller: " + resulted);

		return "user/modify";
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
	public String modify(HttpSession session, @RequestParam String pw, @RequestParam String name,
			@RequestParam String email, @RequestParam String address) {
		String userid = SecurityUtils.getCurrentUserId();
		userService.modify(userid, pw, name, email, address);
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
	
	@RequestMapping("/user/checkId.do")
	@ResponseBody
	public Map<Object, Object> checkId(@RequestParam String userid) {
		int id = userService.getid2(userid);
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
	
	@RequestMapping("/user/checkEmail.do")
	@ResponseBody
	public Map<Object, Object> checkEmail(@RequestParam String email) {
		int id = userService.getid3(email);
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

	@RequestMapping(value = "/user/Searching", method = RequestMethod.POST)
	@ResponseBody
	public List<Member> searchUsers(@RequestParam(required = false) String name, @RequestParam(required = false) String email) {
		// 빈값 처리
		if (name == null || name.isEmpty()) {
			name = null; // name이 빈 문자열일 경우 null로 처리
		}
		if (email == null || email.isEmpty()) {
			email = null; // email이 빈 문자열일 경우 null로 처리
		}

		// 검색 로직을 구현합니다.
		List<Member> users = userService.searchUser(name, email);
		return users;	
	}
	
	 @PostMapping("/user/resetPassword")
	    public String resetPassword(@RequestParam String userid, Model model) {
	        // 비밀번호 초기화
	        String newPassword = UUID.randomUUID().toString().replace("-", "");
	        userService.resetPassword(userid, newPassword);
	        model.addAttribute("message", "비밀번호가 초기화되었습니다.");
	        return "user/usersearch";
	    }	
}


