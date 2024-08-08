package com.example.demo.vo;

import lombok.Data;

@Data
public class Cart {
	private int id;
	private String userid;
	private int productid;	
	private String productname;
	private String color; // 가격에 영향을 주는 기타 옵션들
	private String size;
	private int count;
	private int price;
	private int priceall;

	public Cart(int id, String userid, int productid, String productname, String color, String size, int count, int price, int priceall) {
		this.id = id;
		this.userid = userid;
		this.productid = productid;
		this.productname = productname;
		this.color = color;
		this.size = size;
		this.count = count;
		this.price = price;
		this.priceall = priceall;
	}
}