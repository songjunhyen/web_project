package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dao.CartDao;
import com.example.demo.vo.Cart;

@Service
public class CartService {
	private CartDao cartDao;

	CartService(CartDao cartDao) {
		this.cartDao = cartDao;
	}

	public List<Cart> GetCartList(int userid) {
		return cartDao.GetCartList(userid);
	}

	public void AddCartList(int userid, int productid, String name, String color, String size, int count, int price) {
		cartDao.AddCartList(userid, productid, name, color, size, count, price);
	}

	public void ModifyCartList(int id, int userid, int productid, String productname, String a_color, String a_size,
			String color, String size, int count, int price) {
		// a_붙은게 기존값 없는게 수정된값
		if (checking(userid, productid, a_color, a_size)) {
			// 기존 데이터가 존재할 경우
			if (id != GetCartId(userid, productid, productname, a_color, a_size)) {
				// 해당 제품이 장바구니에 이미 존재하지 않으면 새로 추가
				cartDao.DeleteCartList(id, userid, productid, a_color, a_size); // 기존 아이템 삭제
				cartDao.insertCart(userid, productid, productname, color, size, count, price); // 새로운 아이템 추가
			} else {
				// 해당 제품이 장바구니에 이미 존재하면 차이나는 것만 업데이트
				if (color.equals(a_color) && size.equals(a_size)) {
					cartDao.updateCount(userid, productid, color, size, count); // 수량 업데이트
				}
				if (!color.equals(a_color)) {
					cartDao.updateColor(id,userid, productid, color, size); // 색상 업데이트
				}
				if (!size.equals(a_size)) {
					cartDao.updateSize(id, userid, productid, color, size); // 사이즈 업데이트
				}if(!color.equals(a_color)&&!size.equals(a_size)) {
					cartDao.updateTwo(id, userid, productid, color, size);
				}
			}
		} else {
			// 기존 데이터가 없을 경우 새로운 아이템 추가
			cartDao.insertCart(userid, productid, productname, color, size, count, price); // 새로운 아이템 추가
		}
	}

	public void DeleteCartList(int id, int userid, int productid, String color, String size) {
		cartDao.DeleteCartList(id, userid, productid, color, size);
	}

	public boolean checking(int userid, int productid, String color, String size) {
		int count = cartDao.checking(userid, productid, color, size);
		return count > 0;
	}

	public int GetCartId(int userid, int productid, String productname, String color, String size) {
		return cartDao.GetCartId(userid, productid, productname, color, size);
	}

}
