package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.CartService;
import com.example.demo.vo.Cart;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	private CartService cartservice;

	CartController(CartService cartservice) {
		this.cartservice = cartservice;
	}

	@PostMapping("/Cart/add")
	public String addCartItem(HttpSession session, Model model, @RequestParam int productid, @RequestParam String name,
			@RequestParam String color, @RequestParam String size, @RequestParam int count, @RequestParam int price) {
		int userid = (int) session.getAttribute("id");
		boolean ishave = cartservice.checking(userid, productid, color, size);
		if(!ishave) {
			cartservice.AddCartList(userid, productid, name, color, size, count, price);
		}else {
			int id = cartservice.GetCartId(userid, productid, name, color, size);
			cartservice.ModifyCartList(id, userid, productid, name, color, size, color, size, count, price);
		}
		// return "redirect:/product/detail?id=" + productid;
		return "redirect:/Cart/List";
	}

	@GetMapping("/Cart/List")
	public String GetCartList(HttpSession session, Model model) {
		int userid = (int) session.getAttribute("id");
		List<Cart> carts = cartservice.GetCartList(userid);
		
		model.addAttribute("carts", carts);
		
		return "/cart/cartmain";
	}

	@PostMapping("/Cart/Modify")
	@ResponseBody
	public String ModifyCartList(HttpSession session, @RequestParam int id, @RequestParam String productname, @RequestParam int productid, @RequestParam String a_color,
	        @RequestParam String a_size, @RequestParam String color,
	        @RequestParam String size, @RequestParam int count, @RequestParam int price) {		
		//a_붙은게 수정전 값 그냥이 수정한 값
	    int userid = (int) session.getAttribute("id");
	    cartservice.ModifyCartList(id, userid, productid, productname, a_color, a_size, color, size, count, price);

	    return "success"; // Ajax 요청에 대한 응답
	}

	@PostMapping("/Cart/Delete")
	@ResponseBody
	public String DeleteCartList(HttpSession session, @RequestParam int id, @RequestParam int productid, @RequestParam String color,
			@RequestParam String size) {
		int userid = (int) session.getAttribute("id");
		cartservice.DeleteCartList(id, userid, productid, color, size);
		return "success"; // Ajax 요청에 대한 응답
	}
}
