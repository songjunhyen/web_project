package com.example.demo.vo;

import lombok.Data;

@Data
public class Admin {
  private int id; // num DB쪽에서 자동증가로 할 것
  private String AdminId;
  private String AdminPw;
  private String name;
  private String email;
  private int adminclass = 1; // 관리자 등급. 최상위 관리자(전권환), 상품관리자(id랑 등급으로 본인 상품관리), 회원 관리자, 주문 관리자, 고객센터 관리자
  
  public Admin(String adminId, String adminPw, String name, String email) {
      this.AdminId = adminId;
      this.AdminPw = adminPw;
      this.name = name;
      this.email = email;
  }
}
