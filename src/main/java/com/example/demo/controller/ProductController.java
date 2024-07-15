package com.example.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.ProductService;
import com.example.demo.vo.Product;

@Controller
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
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
	public String detail(@RequestParam int id, Model model) {
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
	
	@GetMapping("/product/list")
	public String list(Model model) {
		List<Product> products = productService.getProductlist();
		Collections.reverse(products);
	    
		model.addAttribute("products", products);
		
		return "product/productlist"; // "product/productadd.jsp"를 반환하도록 설정
	}

	@GetMapping("/product/modify")
	public String modify(Model model,@RequestParam int id) {
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

