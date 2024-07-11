package com.example.demo.vo;

import lombok.Data;

@Data
public class Category {
	  private int categoryId;
	  private String categoryName;
	  private Category parentCategory; // 상위 카테고리
	  private String categorytext; //카테고리 설명?

	  public Category(String categoryName, Category parentCategory, String categorytext) {
	      this.categoryName = categoryName;
	      this.parentCategory = parentCategory;
	      this.categorytext = categorytext;
	  }
	}