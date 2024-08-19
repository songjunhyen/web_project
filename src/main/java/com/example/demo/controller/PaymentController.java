package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

	@PostMapping("/completePurchase") // 결제 성공 시 호출
	public String completePurchase(@RequestParam String orderNumber, @RequestParam int price,
			@RequestParam String phone, @RequestParam String address, @RequestParam String paymentMethod,
			@RequestParam String merchant_uid) {

		LocalDateTime now = LocalDateTime.now();

		// 배송 정보와 결제 완료 정보를 저장하는 로직
		// 예: 주문 상태 업데이트, 배송 정보 저장 등

		// 예시: 주문 상태 업데이트
		// updateOrderStatus(orderNumber, "completed");

		// 예시: 배송 정보 저장
		// saveShippingInfo(orderNumber, phone, address);

		// 결제 완료 페이지로 리디렉션
		return "";
	}

	@PostMapping("/initiateKakaoPay")
	public ResponseEntity<Map<String, Object>> initiateKakaoPay(@RequestParam String orderNumber,
			@RequestParam int price) {

		String merchant_uid = "IMP" + new Date().getTime(); // 결제 고유 번호

		Map<String, Object> paymentData = new HashMap<>();
		paymentData.put("pg", "kakaopay.TC0ONETIME");
		paymentData.put("pay_method", "card");
		paymentData.put("merchant_uid", merchant_uid);
		paymentData.put("name", "상품명");
		paymentData.put("amount", price);

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("paymentData", paymentData);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/initiateNaverPay")
	public ResponseEntity<Map<String, Object>> initiateNaverPay(@RequestParam String orderNumber,
			@RequestParam int price) {

		String redirectUrl = "https://pay.naver.com/?amount=" + price + "&order_id=" + orderNumber;

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("redirectUrl", redirectUrl);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/initiateTossPay")
	public ResponseEntity<Map<String, Object>> initiateTossPay(@RequestParam String orderNumber,
			@RequestParam int price) {

		String redirectUrl = "https://toss.im/checkout?amount=" + price + "&order_id=" + orderNumber;

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("redirectUrl", redirectUrl);

		return ResponseEntity.ok(response);

	}
}
