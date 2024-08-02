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

		int pageSize = 5; // 한 페이지에 보여줄 게시물 수
		int totalCount = products.size();
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);
		int start = (page - 1) * pageSize;
		int end = Math.min(start + pageSize, totalCount);

		List<Product> paginatedProducts = products.subList(start, end);

		model.addAttribute("products", paginatedProducts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		
		 // 페이지네이션 계산
	    int[] pagination = calculatePagination(page, totalPages);
	    int startPage = pagination[0];
	    int endPage = pagination[1];

	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
		
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

		int pageSize = 5; // 한 페이지에 보여줄 게시물 수
		int totalCount = products.size();
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);
		int start = (page - 1) * pageSize;
		int end = Math.min(start + pageSize, totalCount);

		List<Product> paginatedProducts = products.subList(start, end);

		model.addAttribute("products", paginatedProducts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		
		 // 페이지네이션 계산
	    int[] pagination = calculatePagination(page, totalPages);
	    int startPage = pagination[0];
	    int endPage = pagination[1];

	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
		
		return "realMain"; // "product/main.jsp"를 반환하도록 설정
	}
	
	public static int[] calculatePagination(int currentPage, int totalPages) {
        // Null 체크 및 기본값 설정
        if (currentPage <= 0) {
            currentPage = 1;
        }
        if (totalPages <= 0) {
            totalPages = 1;
        }
        int MAX_PAGES_TO_SHOW = 10;
        int startPage, endPage;

        // 페이지 수가 MAX_PAGES_TO_SHOW 이하일 때는 모든 페이지를 보여줍니다.
        if (totalPages <= MAX_PAGES_TO_SHOW) {
            startPage = 1;
            endPage = totalPages;
        } else {
            // 시작 페이지와 끝 페이지 계산
            startPage = Math.max(1, currentPage - MAX_PAGES_TO_SHOW / 2);
            endPage = Math.min(totalPages, startPage + MAX_PAGES_TO_SHOW - 1);

            // 페이지 범위를 조정하여 최대 MAX_PAGES_TO_SHOW 개를 초과하지 않도록 합니다.
            if (endPage - startPage + 1 < MAX_PAGES_TO_SHOW) {
                startPage = Math.max(1, endPage - MAX_PAGES_TO_SHOW + 1);
            }
        }

        return new int[]{startPage, endPage};
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
