package com.example.demo.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Specifications {
  private String color;
  private String size;
  private List<String> additionalOptions; // 가격에 영향을 주는 기타 옵션들

  public Specifications(String color, String size) {
    this.color = color;
    this.size = size;
    this.additionalOptions = new ArrayList<>();
  }
}
