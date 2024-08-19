package com.example.demo.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NonMemberPurchaseInfo {
	private String orderNumber;
    private String email;
    private String phonenum;
    private String productid;  // 제품 ID (문자열로 처리)
    private String productname;
    private String sizecolor; // 옵션 (사이즈, 색상 등)
    private int price;
    private LocalDateTime requestDate;
    private String guestName;  // 추가된 필드
    private String guestAddress;
    private int quantity; // 추가된 필드
}
