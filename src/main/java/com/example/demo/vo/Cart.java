package com.example.demo.vo;

import lombok.Data;

@Data
public class Cart {
	private int userid;
	private int productid;	
	private String productname;
	private String options ; // 가격에 영향을 주는 기타 옵션들
	private int count;

	public Cart(int userid,	int productid, String productname, String options, int count) {
	    this.userid = userid;
	    this.productid = productid;
	    this.productname = productname;
	    this.options = options;
	    this.count = count;
	  }
	}