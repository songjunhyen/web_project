package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.service.CartService;
import com.example.demo.util.SecurityUtils;
import com.example.demo.util.SessionFileUtil;
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
		String userid = SecurityUtils.getCurrentUserId();
		boolean ishave = cartservice.checking(userid, productid, color, size);
		if (!ishave) {
			cartservice.AddCartList(userid, productid, name, color, size, count, price);
		} else {
			int id = cartservice.GetCartId(userid, productid, name, color, size);
			cartservice.ModifyCartList(id, userid, productid, name, color, size, color, size, count, price);
		}
		// return "redirect:/product/detail?id=" + productid;
		return "redirect:/Cart/List";
	}

	@GetMapping("/Cart/List")
	public String GetCartList(HttpSession session, Model model) {
		String userid = SecurityUtils.getCurrentUserId();	    
	    
	    // 장바구니 목록 가져오기
	    List<Cart> carts = cartservice.GetCartList(userid); // 서비스 메서드 호출
	    
	    // 장바구니 목록을 모델에 추가
	    model.addAttribute("userid", userid);
	    model.addAttribute("carts", carts);
	    
	    // 뷰 이름 반환
	    return "cart/cartmain";
	}

	@PostMapping("/Cart/Modify")
	public String ModifyCartList(HttpSession session, @RequestParam int id, @RequestParam String productname,
			@RequestParam int productid, @RequestParam String a_color, @RequestParam String a_size,
			@RequestParam String color, @RequestParam String size, @RequestParam int count, @RequestParam int price) {
		// a_붙은게 수정전 값 그냥이 수정한 값
		String userid = SecurityUtils.getCurrentUserId();	   
		cartservice.ModifyCartList(id, userid, productid, productname, a_color, a_size, color, size, count, price);

		return "redirect:/Cart/List";// Ajax 요청에 대한 응답
	}

	@PostMapping("/Cart/Delete")
	public String DeleteCartList(HttpSession session, @RequestParam int id, @RequestParam int productid,
			@RequestParam String color, @RequestParam String size) {
		String userid = SecurityUtils.getCurrentUserId();	   
		cartservice.DeleteCartList(id, userid, productid, color, size);
		return "redirect:/Cart/List"; // Ajax 요청에 대한 응답
	}

	@PostMapping("/Temporarily/Cart/add")
	public String addCartTemp(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			Model model, @RequestParam int productid, @RequestParam String name, @RequestParam String color,
			@RequestParam String size, @RequestParam int count, @RequestParam int price) {
		String sessionId = session.getId();
		Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
		sessionCookie.setMaxAge(86400); // 1 day
		sessionCookie.setPath("/");
		response.addCookie(sessionCookie);

		List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
		if (cartItems == null) {
			try {
				try {
					cartItems = SessionFileUtil.loadSession(sessionId);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				cartItems = new ArrayList<>();
			}
			session.setAttribute("cartItems", cartItems);
		}

		boolean updated = false;
		for (CartItem item : cartItems) {
			if (item.getProductid() == productid && item.getName().equals(name) && item.getColor().equals(color)
					&& item.getSize().equals(size)) {
				item.setCount(item.getCount() + count);
				updated = true;
				break;
			}
		}

		if (!updated) {
			cartItems.add(new CartItem(productid, name, color, size, count, price));
		}

		try {
			SessionFileUtil.saveSession(sessionId, cartItems);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("carts", cartItems);

		return "redirect:/temp/Cart";
	}

	@GetMapping("/temp/Cart")
	public String TempCartList(HttpSession session, HttpServletRequest request, Model model) {
		// 쿠키에서 세션 ID 가져오기
		String sessionId = session.getId();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("SESSIONID")) {
					sessionId = cookie.getValue();
					break;
				}
			}
		}

		// 세션에서 장바구니 항목 가져오기
		List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
		if (cartItems == null) {
			try {
				try {
					cartItems = SessionFileUtil.loadSession(sessionId);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				cartItems = new ArrayList<>();
			}
			session.setAttribute("cartItems", cartItems);
		}

		model.addAttribute("carts", cartItems);
		return "cart/tempcart";
	}

	@PostMapping("/Temporarily/Cart/Modify")
    public RedirectView modifyCartItem(@RequestParam int productid, @RequestParam String color,
                                        @RequestParam String size, @RequestParam int count, @RequestParam int price,
                                        HttpSession session) {

        String sessionId = session.getId();
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

        if (cartItems == null) {
            try {
                cartItems = SessionFileUtil.loadSession(sessionId);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                cartItems = new ArrayList<>();
            }
            session.setAttribute("cartItems", cartItems);
        }

        boolean updated = false;
        for (CartItem item : cartItems) {
            if (item.getProductid() == productid && item.getColor().equals(color) && item.getSize().equals(size)) {
                item.setCount(count);
                updated = true;
                break;
            }
        }

        if (!updated) {
            cartItems.add(new CartItem(productid, "Product Name", color, size, count, price));
        }

        try {
            SessionFileUtil.saveSession(sessionId, cartItems);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 리다이렉션 처리
        return new RedirectView("/temp/Cart");
    }

    @PostMapping("/Temporarily/Cart/Delete")
    public RedirectView deleteCartItem(@RequestParam int productid, @RequestParam String color,
                                        @RequestParam String size, HttpSession session) {

        String sessionId = session.getId();
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

        if (cartItems == null) {
            try {
                cartItems = SessionFileUtil.loadSession(sessionId);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                cartItems = new ArrayList<>();
            }
            session.setAttribute("cartItems", cartItems);
        }

        cartItems.removeIf(item -> item.getProductid() == productid && item.getColor().equals(color) && item.getSize().equals(size));

        try {
            SessionFileUtil.saveSession(sessionId, cartItems);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 리다이렉션 처리
        return new RedirectView("/temp/Cart");
    }
}
