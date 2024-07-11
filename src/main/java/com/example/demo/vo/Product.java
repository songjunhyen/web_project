package com.example.demo.vo;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class Product {
  private int id; // num DB쪽에서 자동증가로 할 것
  private String name;
  private int price;
  private String description; // 설명
  //private String imageUrl;
  private int count; // 재고량
  private String category; // 카테고리 
  private String maker; // 제조사
  private String color;
  private String size;
  private List<String> additionalOptions; 
  private LocalDate regDate = LocalDate.now(); // 등록일
  private int viewcount = 0;

  public Product(String name, int price, String description, //String imageUrl,
                   int count, String category , String maker, String color, String size) {
    this.name = name;
    this.price = price;
    this.description = description;
    //this.imageUrl = imageUrl;
    this.count = count;
    this.category = category;
    this.maker = maker;
    this.color = color;
    this.size = size;
  }
}