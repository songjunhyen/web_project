package com.example.demo.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.ProductService;
import com.example.demo.vo.Product;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/Home/Main")
	public String mainPage(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {

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

	@GetMapping("/test/product/Main")
	public String mainPage() {
		return "product/main"; // "product/main.jsp"를 반환하도록 설정
	}

	@GetMapping("/product/add")
	public String write() {
		return "product/productadd"; // "product/productadd.jsp"를 반환하도록 설정
	}

	@GetMapping("/product/detail")
	public String detail(HttpSession session, @RequestParam int id, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		boolean result = productService.searchProduct(id);
		if (!result) {
			model.addAttribute("message", "오류가 발생하였습니다.");
			return "error";
		} else {
			String writerid = productService.getwriterid(id);
			String userid = (String) session.getAttribute("userid");

			Product product = productService.ProductDetail(id);

			if (writerid.equals(userid)) {
				model.addAttribute("product", product);
				return "product/productdetail";
			} else {
				Cookie[] cookies = request.getCookies();
				boolean shouldUpdateViewCount = true;
				LocalDateTime now = LocalDateTime.now();
				String productCookieName = "viewedProduct_" + id;

				if (cookies != null) {
					for (Cookie cookie : cookies) {
						if (cookie.getName().equals(productCookieName)) {
							// 쿠키에서 저장된 시간 추출
							LocalDateTime lastVisitTime = LocalDateTime.parse(cookie.getValue());
							if (Duration.between(lastVisitTime, now).toMinutes() <= 30) {
								shouldUpdateViewCount = false;
							}
							break;
						}
					}
				}

				// 쿠키 설정: 제품 ID와 방문 시간을 저장
				if (shouldUpdateViewCount) {
					Cookie viewedCookie = new Cookie(productCookieName, now.toString());
					viewedCookie.setMaxAge(3600); // 쿠키 유효 시간 1시간
					response.addCookie(viewedCookie);

					// 조회수 증가
					productService.updateViewCount(id);
				}
				model.addAttribute("product", product);
				return "product/productdetail";
			}
		}
	}

	@GetMapping("/product/list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
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
		return "product/productlist"; // "product/productadd.jsp"를 반환하도록 설정
	}

	@GetMapping("/product/modify")
	public String modify(Model model, @RequestParam int id) {
		model.addAttribute("productId", id);
		return "product/productmodify"; // "product/productadd.jsp"를 반환하도록 설정
	}

	@PostMapping("/product/ADD")
	public String addProduct(HttpSession session, Model model, @RequestParam String name, @RequestParam int price,
			@RequestParam String description, @RequestParam int count, @RequestParam String category,
			@RequestParam String maker, @RequestParam String color, @RequestParam String size,
			@RequestParam String options) {

		String userid = (String) session.getAttribute("userid");
		Product product = new Product(0,userid, name, price, description, count, category, maker, color, size, "");
		product.setAdditionalOptions(options);

		productService.addProduct(product);
		model.addAttribute("product", product);
		return "product/main";
	}

	@PostMapping("/product/Detail")
	public String ProductDetail(Model model, @RequestParam int id) {
		boolean result = productService.searchProduct(id);
		if (!result) {
			model.addAttribute("message", "제품 추가 중 오류가 발생하였습니다.");
			return "error";
		} else {
			Product product = productService.ProductDetail(id);
			model.addAttribute("product", product);
			return "product/productdetail";
		}
	}

	@PostMapping("/product/Modify")
	public String modifyProduct(HttpSession session, Model model, @RequestParam int productId, @RequestParam String name,
			@RequestParam int price, @RequestParam String description, @RequestParam int count,
			@RequestParam String category, @RequestParam String maker, @RequestParam String color,
			@RequestParam String size, @RequestParam List<String> options) {

		boolean result = productService.searchProduct(productId);

		if (!result) {
			model.addAttribute("message", "제품 수정 중 오류가 발생하였습니다.");
			return "error";
		} else {
			String userid = (String) session.getAttribute("userid");
			Product product = new Product(0,userid, name, price, description, count, category, maker, color, size, "");
			productService.modifyProduct(productId, product);
			model.addAttribute("product", product);
			return "redirect:/product/detail?id=" + productId;
		}
	}

	@PostMapping("/product/Delete")
	public String deleteProduct(Model model, @RequestParam int id) {
		boolean result = productService.searchProduct(id);
		if (!result) {
			model.addAttribute("message", "제품 삭제 중 오류가 발생하였습니다.");
			return "error";
		} else {
			productService.deleteProduct(id);
			model.addAttribute("productId", id);
			return "redirect:/product/list";
		}
	}
}
