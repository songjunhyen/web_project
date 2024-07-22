package com.example.demo.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.CartService;
import com.example.demo.vo.Cart;
import com.example.demo.vo.CartItem;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	
	@PostMapping("/Temporarily/Cart/add")
	public String addCartTemp(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model,
	                           @RequestParam int productid, @RequestParam String name,
	                           @RequestParam String color, @RequestParam String size,
	                           @RequestParam int count, @RequestParam int price) {
	    String cartCookiePrefix = "cartItem_";
	    Cookie[] cookies = request.getCookies();
	    Map<String, Cookie> existingCookies = new HashMap<>();

	    // 기존 쿠키 읽어오기
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().startsWith(cartCookiePrefix)) {
	                existingCookies.put(cookie.getName(), cookie);
	            }
	        }
	    }

	    // 새 항목 쿠키 이름 및 값 생성
	    String newItemCookieName = cartCookiePrefix + productid + "_" + name + "_" + color + "_" + size;
	    String newItemCookieValue = productid + ":" + name + ":" + color + ":" + size + ":" + count + ":" + price;

	    // 기존 쿠키 중 해당 상품과 동일한 항목이 있는지 확인하고 업데이트
	    boolean updated = false;
	    for (Map.Entry<String, Cookie> entry : existingCookies.entrySet()) {
	        Cookie existingCookie = entry.getValue();
	        String[] details = existingCookie.getValue().split(":");
	        if (details.length == 6 &&
	            Integer.parseInt(details[0]) == productid &&
	            details[1].equals(name) &&
	            details[2].equals(color) &&
	            details[3].equals(size)) {

	            // 항목이 이미 존재하면 수량 업데이트
	            int existingCount = Integer.parseInt(details[4]);
	            int newCount = existingCount + count;
	            existingCookie.setValue(productid + ":" + name + ":" + color + ":" + size + ":" + newCount + ":" + price);
	            existingCookie.setMaxAge(86400); // 쿠키 유효 시간 1일
	            existingCookie.setPath("/"); // 모든 경로에서 유효
	            response.addCookie(existingCookie); // 업데이트된 쿠키 재설정
	            updated = true;
	            break;
	        }
	    }

	    // 기존 항목이 없으면 새로운 쿠키로 추가됨
	    if (!updated) {
	        Cookie newItemCookie = new Cookie(newItemCookieName, newItemCookieValue);
	        newItemCookie.setMaxAge(86400); // 쿠키 유효 시간 1일
	        newItemCookie.setPath("/"); // 모든 경로에서 유효
	        response.addCookie(newItemCookie);
	    }

	    // 장바구니 데이터 읽기 및 모델에 추가
	    List<CartItem> cartItems = new ArrayList<>();
	    for (Cookie cookie : request.getCookies()) {
	        if (cookie.getName().startsWith(cartCookiePrefix)) {
	            String[] details = cookie.getValue().split(":");
	            if (details.length == 6) {
	                try {
	                    CartItem cartItem = new CartItem(
	                        Integer.parseInt(details[0]),
	                        details[1],
	                        details[2],
	                        details[3],
	                        Integer.parseInt(details[4]),
	                        Integer.parseInt(details[5])
	                    );
	                    cartItems.add(cartItem);
	                } catch (NumberFormatException e) {
	                    // 숫자 파싱 오류 처리
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

	    model.addAttribute("carts", cartItems);
	    return "redirect:/temp/Cart";
	}
	
	@GetMapping("/temp/Cart")
	public String TempCartList(HttpServletRequest request, Model model) {
	    String cartCookiePrefix = "cart";
	    Cookie[] cookies = request.getCookies();
	    List<CartItem> cartItems = new ArrayList<>();

	    // 쿠키가 존재하는지 확인
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().startsWith(cartCookiePrefix)) {
	                String cartData = cookie.getValue();
	                System.out.println("Cart Data from " + cookie.getName() + ": " + cartData); // 로그 추가

	                // 쿠키 데이터 처리
	                if (cartData != null && !cartData.isEmpty()) {
	                    String[] items = cartData.split("\\|");
	                    for (String item : items) {
	                        String[] details = item.split(":");
	                        if (details.length == 6) {
	                            try {
	                                CartItem cartItem = new CartItem(
	                                    Integer.parseInt(details[0]),
	                                    details[1],
	                                    details[2],
	                                    details[3],
	                                    Integer.parseInt(details[4]),
	                                    Integer.parseInt(details[5])
	                                );
	                                cartItems.add(cartItem);
	                            } catch (NumberFormatException e) {
	                                // 숫자 파싱 오류 처리
	                                e.printStackTrace();
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }

	    // 장바구니 데이터 모델에 추가
	    model.addAttribute("carts", cartItems);
	    System.out.println("Cart Items: " + cartItems); // 로그 추가

	    // 템플릿 반환
	    return "cart/tempcart";
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
