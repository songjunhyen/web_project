package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);


	MainController(ProductService productService, AllService allService) {
		this.productService = productService;
		this.allService = allService;
	}

	@GetMapping("/")
	public String home(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		String userRole = "";
		int adminClass = 10;
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

		return new int[] { startPage, endPage };
	}

	@GetMapping("/Home/login")
	public String Login(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false); 
		if (session != null) {
		    session.invalidate();
		    logger.info("Session invalidated: {}", session.getId()); // 세션 ID 출력
		} else {
		    logger.info("No session found to invalidate.");
		}
		return "AllLogin";
	}

	@PostMapping("/Home/logout")
	public String logout(@AuthenticationPrincipal OAuth2User principal, HttpServletRequest request, HttpServletResponse response) {
	    Logger logger = LoggerFactory.getLogger(getClass());

	    // 스프링 시큐리티 로그아웃 처리
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null) {
	        new SecurityContextLogoutHandler().logout(request, response, authentication);
	        logger.info("Spring Security logout handler invoked");
	    } else {
	        logger.info("No authentication found for logout.");
	    }
	    
	    // 세션 무효화
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	        logger.info("Session invalidated");
	    }	    
	    
	    // 쿠키 삭제
	    Cookie cookie = new Cookie("JSESSIONID", null);
	    cookie.setPath("/");
	    cookie.setDomain("localhost"); // 도메인 설정
	    cookie.setHttpOnly(true);
	    cookie.setSecure(false); // HTTPS를 사용하는 경우 true로 설정
	    cookie.setMaxAge(0); // 쿠키 만료
	    response.addCookie(cookie);
	    
	    // 캐시 무효화 헤더 설정
	    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    response.setHeader("Pragma", "no-cache");
	    response.setDateHeader("Expires", 0);

	    // 구글 로그아웃 리다이렉션
	    if (principal != null) {
	        try {
	            String redirectUrl = "http://localhost:8082/Home/Main";
	            String encodedUrl = URLEncoder.encode(redirectUrl, "UTF-8");
	            String logoutUrl = "https://accounts.google.com/Logout?continue=" + encodedUrl;
	            logger.info("Redirecting to Google logout URL: {}", logoutUrl);
	            return "redirect:" + logoutUrl;
	        } catch (UnsupportedEncodingException e) {
	            logger.error("Encoding not supported: ", e);
	            return "redirect:/Home/Main"; // 인코딩 실패 시 기본 리다이렉션
	        }
	    }

	    return "redirect:/Home/Main";
	}
	
	@GetMapping("/api/auth/check")
	public ResponseEntity<String> checkAuthentication(HttpServletRequest request) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication instanceof AnonymousAuthenticationToken) {
	        return ResponseEntity.ok("User is not authenticated");
	    } else if (authentication != null && authentication.isAuthenticated()) {
	        return ResponseEntity.ok("Authenticated");
	    }
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not Authenticated");
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
