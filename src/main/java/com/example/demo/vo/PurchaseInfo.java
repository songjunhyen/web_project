package com.example.demo.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PurchaseInfo {
    private String orderNumber;
    private String userid;
    private String cartids;  // 카트 ID (회원 구매용)
    private int productid;
    private String productname;
    private String sizecolor; // 옵션 (사이즈, 색상 등)
    private int price;
    private LocalDateTime requestDate;
    private int quantity; // 추가된 필드
    private String phone; // 추가된 필드
    private String address; // 추가된 필드
    private String email;
}