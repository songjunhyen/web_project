package com.example.demo.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentInfo {
    private String orderNumber; // 주문 번호
    private int price; // 결제 금액
    private String phone; // 전화번호
    private String address; // 주소
    private String paymentMethod; // 결제 방법
    private String paymentStatus; // 결제 상태
    private LocalDateTime paymentDate; // 결제 일시
}