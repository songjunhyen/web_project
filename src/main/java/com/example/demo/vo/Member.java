package com.example.demo.vo;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class Member {
  private int id = 0; // num DB쪽에서 자동증가로 할 것
  private LocalDate regdate = LocalDate.now(); // 가입일
  private String userid;
  private String userpw;
  private String name;
  private String email;
  private String address; // 주소
  private int memberClass = 0;
  private List<Product> products; // 상품정보(구입한?)

  public Member(String userid, String userpw, String name, String email, String address) {
    this.userid = userid;
    this.userpw = userpw;
    this.name = name;
    this.email = email;
    this.address = address;
  }
  
  public Member() {
	  
  }

}

