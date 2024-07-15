package com.example.demo.vo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Product {
  private int id; // num DB쪽에서 자동증가로 할 것
  private String name;
  private int price;
  private String description; // 설명
  private int count; // 재고량
  private String category; // 카테고리 
  private String maker; // 제조사
  private String color;
  private String size;
  private String additionalOptions; 
  private LocalDate regDate; // 등록일
  private int viewcount;

	public Product(int id, String name, int price, String description, int count, String category, String maker, String color,
			String size, String additionalOptions) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
//this.imageUrl = imageUrl;
		this.count = count;
		this.category = category;
		this.maker = maker;
		this.color = color;
		this.size = size;
		this.additionalOptions = additionalOptions;
		this.regDate = LocalDate.now();
		this.viewcount = 0;
	}
}