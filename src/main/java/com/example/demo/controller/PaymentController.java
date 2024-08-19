package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@PostMapping("/initiateKakaoPay")
	public ResponseEntity<Map<String, Object>> initiateKakaoPay(@RequestParam String orderNumber, @RequestParam int price) {
	    Map<String, Object> response = new HashMap<>();
	    // 카카오페이 결제 데이터 생성 로직
	    String merchantUid = generateMerchantUid();
	    String payAuthId = generatePayAuthId();
	    
	    
	    //오더넘버로 주문테이블에서 저장된 상품명 가져오도록하자
	    // 주문번호로 상품 정보 조회
	    //OrderInfo orderInfo = orderService.getOrderInfoByOrderNumber(orderNumber); // 서비스 메서드를 호출하여 주문 정보를 가져옵니다.	    
	    //if (orderInfo == null) {
	    //    response.put("status", "error");
	    //    response.put("message", "주문 정보를 찾을 수 없습니다.");
	    //    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    //}
	    
	    logger.info("Initiating KakaoPay with orderNumber: {}, price: {}, merchantUid: {}, payAuthId: {}", 
                orderNumber, price, merchantUid, payAuthId);
	    
	    // 예시 결제 데이터
	    Map<String, Object> paymentData = new HashMap<>();
	    paymentData.put("merchant_uid", merchantUid);
	    paymentData.put("pay_auth_id", payAuthId);
	    paymentData.put("amount", price);
	    paymentData.put("name", "상품명"); // 실제 상품명으로 변경

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
	
	public String generateMerchantUid() {
	    // 거래를 위한 고유한 식별자를 생성합니다.
	    return "order_" + UUID.randomUUID().toString().replace("-", "");
	}
	public String generatePayAuthId() {
	    // 결제를 위한 고유한 인증 식별자를 생성합니다.
	    return "auth_" + UUID.randomUUID().toString().replace("-", "");
	}
}
