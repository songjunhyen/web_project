package com.example.demo.vo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Review {
	  private int id; // 리뷰 id
	  private int productId; // 상품 id
	  private String writer; // 작성자
	  private LocalDate regDate; // 작성일
	  private String reviewText;
	  private double star; // 별점 소수점 1자리수까지 1~5

	  public Review(int id, int productId, String writer, LocalDate regDate, String reviewText, double star) {
	    this.id = id;
	    this.productId = productId;
	    this.writer = writer;
	    this.regDate = regDate;
	    this.reviewText = reviewText;
	    this.star = star;   
	  }
	}
