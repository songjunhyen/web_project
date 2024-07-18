package com.example.demo.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.ProductService;
import com.example.demo.vo.Product;

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
	public String detail(HttpSession session, @RequestParam int id, Model model) {
		boolean result = productService.searchProduct(id);
		if (!result) {
			model.addAttribute("message", "오류가 발생하였습니다.");
			return "error";
		} else {
			Product product = productService.ProductDetail(id);

			// 작성자 본인인지 체크하고 몇초이내에 들어는지 체크 아니면 viewcountup
			// String user = (String) session.getAttribute("userid");
			// int userid = (String) session.getAttribute("id");
			// 값이 없을 경우(비로그인)인 경우 고려
			// productService.getwriter(id);해서 작성자 가져온 다음
			// 비교해서 아니고 비로그인 고려해서 viewcountup할지 말지 결정
			// productService.ViewcountUp(id);
			// 사용자 식별 코드를 쿠키로 부여하는 것은 사용자를 식별하고 중복 조회를 막는 좋은 방법
			model.addAttribute("product", product);
			return "product/productdetail";
		}
	}

	@GetMapping("/product/list")
	public String list(Model model) {
		List<Product> products = productService.getProductlist();
		Collections.reverse(products);

		model.addAttribute("products", products);

		return "product/productlist"; // "product/productadd.jsp"를 반환하도록 설정
	}

	@GetMapping("/product/modify")
	public String modify(Model model, @RequestParam int id) {
		model.addAttribute("productId", id);
		return "product/productmodify"; // "product/productadd.jsp"를 반환하도록 설정
	}

	@PostMapping("/product/ADD")
	public String addProduct(Model model, @RequestParam String name, @RequestParam int price,
			@RequestParam String description, @RequestParam int count, @RequestParam String category,
			@RequestParam String maker, @RequestParam String color, @RequestParam String size,
			@RequestParam String options) {

		Product product = new Product(0, name, price, description, count, category, maker, color, size, "");
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
	public String modifyProduct(Model model, @RequestParam int productId, @RequestParam String name,
			@RequestParam int price, @RequestParam String description, @RequestParam int count,
			@RequestParam String category, @RequestParam String maker, @RequestParam String color,
			@RequestParam String size, @RequestParam List<String> options) {

		boolean result = productService.searchProduct(productId);

		if (!result) {
			model.addAttribute("message", "제품 수정 중 오류가 발생하였습니다.");
			return "error";
		} else {
			Product product = new Product(0, name, price, description, count, category, maker, color, size, "");
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
