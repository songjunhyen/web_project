package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.CartService;
import com.example.demo.vo.Cart;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	private CartService cartservice;
	
	CartController(CartService cartservice){
		this.cartservice = cartservice;
	}

	@GetMapping("/Cart/List")
	public String GetCartList(HttpSession session, Model model){
		int userid = (int) session.getAttribute("id");
		List<Cart> carts = cartservice.GetCartList(userid);
		model.addAttribute("carts",carts);
		return "cart/cartlist"; 		
	}
	
	@PostMapping("/Cart/add")
	public void AddCartList(HttpSession session, @RequestParam int productid, @RequestParam  String color, @RequestParam  String size, @RequestParam  int count){
		int userid = (int) session.getAttribute("id");
		cartservice.AddCartList(userid, productid, color,size,count);
	}
	
	public void ModifyCartList(HttpSession session, @RequestParam int productid, @RequestParam  String color, @RequestParam  String size, @RequestParam  int count){
		int userid = (int) session.getAttribute("id");
		cartservice.ModifyCartList(userid, productid,color,size,count);
	}
	
	public void DeleteCartList(HttpSession session, @RequestParam int productid, @RequestParam  String color, @RequestParam  String size){
		int userid = (int) session.getAttribute("id");
		cartservice.DeleteCartList(userid, productid,color,size);
	}
	
}
