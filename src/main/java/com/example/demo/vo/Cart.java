package com.example.demo.vo;

import lombok.Data;

@Data
public class Cart {
	  private Specifications specifications ; // 가격에 영향을 주는 기타 옵션들
	  private int count;

	  public Cart(Specifications specifications, int count) {
	    this.specifications = specifications;
	    this.count = count;
	  }
	}