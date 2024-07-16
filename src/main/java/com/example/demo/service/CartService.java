package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dao.CartDao;
import com.example.demo.vo.Cart;

@Service
public class CartService {
	private CartDao cartDao;
	
	CartService(CartDao cartDao){
		this.cartDao = cartDao;
	}

	public List<Cart> GetCartList(int userid) {		
		return cartDao.GetCartList(userid);
	}

	public void AddCartList(int userid, int productid, String name, String color, String size, int count, int price) {
		cartDao.AddCartList(userid, productid, name, color, size, count, price);
	}

	public void ModifyCartList(int userid, int productid, String color, String size, int count) {
		cartDao.ModifyCartList(userid, productid, color,  size, count);
	}

	public void DeleteCartList(int userid, int productid, String color, String size) {
		cartDao.DeleteCartList(userid, productid, color,  size);
	}

}
