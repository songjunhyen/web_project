package com.example.demo.vo;

import lombok.Data;

//관리자(Admin)(관리자 ID,관리자 이름,관리자 이메일,관리자 권한 (상품 관리, 회원 관리, 주문 관리 등))
//미리 관리자 생성해놓고 각 관리자에게 배포하여 사용. 
//만약 새로운 인원이 들어왔을 경우랑 관리자가 퇴직한 경우를 대비하여 최상위 관리자만 사용가능한 관리폐이지가 있고
//각 관리자 생성,삭제 버튼이 존재. 단점은 유출됬을 경우가 위험하니 관리자가 작업했을 경우 test용으로 먼저 적용하고 최상위 관리자가 승인하면 적용되도록??
@Data
public class Admin {
  private int id; // num DB쪽에서 자동증가로 할 것
  private String AdminId;
  private String AdminPw;
  private String name;
  private String email;
  private int adminClass = 0; // 관리자 등급. 최상위 관리자(전권환), 상품관리자(id랑 등급으로 본인 상품관리), 회원 관리자, 주문 관리자, 고객센터 관리자
  public Admin(String adminId, String adminPw, String name, String email) {
      this.AdminId = adminId;
      this.AdminPw = adminPw;
      this.name = name;
      this.email = email;
  }
}
