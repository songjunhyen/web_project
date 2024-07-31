package com.example.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.AllService;
import com.example.demo.service.ProductService;
import com.example.demo.util.SecurityUtils;
import com.example.demo.vo.Product;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	private final AllService allService;
	private final ProductService productService;

	MainController(ProductService productService, AllService allService) {
		this.productService = productService;
		this.allService = allService;
	}

	@GetMapping("/")
	public String home(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		String userRole = "";
		int adminClass = 5;
		String userId = SecurityUtils.getCurrentUserId();
		if (userId != null && !userId.equals("Anonymous")) {
			userRole = allService.isuser(userId);
			if (userRole.equals("admin")) {
				adminClass = allService.getadminclass(userId);
			}
			model.addAttribute("userRole", userRole);
			model.addAttribute("adminClass", adminClass);
		}

		List<Product> products = productService.getProductlist();
		Collections.reverse(products);

		int pageSize = 10; // 한 페이지에 보여줄 게시물 수
		int totalCount = products.size();
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);
		int start = (page - 1) * pageSize;
		int end = Math.min(start + pageSize, totalCount);

		List<Product> paginatedProducts = products.subList(start, end);

		model.addAttribute("products", paginatedProducts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		return "realMain";
	}

	@GetMapping("/Home/Main")
	public String mainPage(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		String userRole = "";
		int adminClass = 5;
		String userId = SecurityUtils.getCurrentUserId();
		if (userId != null && !userId.equals("Anonymous")) {
			userRole = allService.isuser(userId);
			if (userRole.equals("admin")) {
				adminClass = allService.getadminclass(userId);
			}
			model.addAttribute("userRole", userRole);
			model.addAttribute("adminClass", adminClass);
		}

		List<Product> products = productService.getProductlist();
		Collections.reverse(products);

		int pageSize = 10; // 한 페이지에 보여줄 게시물 수
		int totalCount = products.size();
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);
		int start = (page - 1) * pageSize;
		int end = Math.min(start + pageSize, totalCount);

		List<Product> paginatedProducts = products.subList(start, end);

		model.addAttribute("products", paginatedProducts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		return "realMain"; // "product/main.jsp"를 반환하도록 설정
	}

	@GetMapping("/Home/login")
	public String Login(HttpServletRequest request, HttpServletResponse response) {
		return "AllLogin";
	}

	@PostMapping("/Home/logout")
	public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		session.invalidate();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// JSESSIONID 쿠키 삭제
				if ("JSESSIONID".equals(cookie.getName())) {
					cookie.setValue(null);
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}

				// CSRF 토큰 쿠키 삭제 (XSRF-TOKEN 예시)
				if ("XSRF-TOKEN".equals(cookie.getName())) {
					cookie.setValue(null);
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}

				// 기타 토큰 삭제 (예시로 AUTH-TOKEN)
				if ("AUTH-TOKEN".equals(cookie.getName())) {
					cookie.setValue(null);
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
		return "redirect:/Home/Main";
	}

	/*
	 * @PostMapping("/Home/Login") public String login(HttpSession session, Model
	 * model) {
	 * 
	 * String roles = (String) session.getAttribute("roles");
	 * model.addAttribute("roles", roles);
	 * 
	 * if ("Admin".equalsIgnoreCase(roles)) {
	 * 
	 * session.setAttribute("islogined", 1); session.setAttribute("class", "admin");
	 * // 세션에 role 값을 저장 session.setAttribute("role", roles); return
	 * "redirect:/Home/Main";
	 * 
	 * } else if ("User".equalsIgnoreCase(roles)) {
	 * 
	 * session.setAttribute("islogined", 1); session.setAttribute("class", "user");
	 * // 세션에 role 값을 저장 session.setAttribute("role", roles); return
	 * "redirect:/Home/Main"; }
	 * 
	 * return "redirect:/Home/Main"; }
	 */
}
