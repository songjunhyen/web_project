package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.vo.PaymentInfo;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Controller
public class PaymentController {
	
	private final IamportClient iamportClient;

    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.api.secret}")
    private String apiSecret;
	
    public PaymentController() {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }
    
	@ResponseBody
    @RequestMapping("/verify/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(imp_uid);
    }
	
	@PostMapping("/pay/completePurchase") // 결제 성공 시 호출 -> 결제 검증 
	 public ResponseEntity<?> completePayment(@RequestParam String imp_uid, 
             @RequestParam String merchant_uid,
             @RequestParam String pay_auth_id,
             @RequestParam String orderNumber) {
		/*
		 결제 성공 후 결제 정보를 받아오기:
		
		결제 시스템(카카오페이, 네이버페이, 토스페이)에서 제공하는 결제 성공 응답 데이터를 받아옵니다. 이 데이터에는 거래 고유 ID, 결제 금액, 결제 상태 등의 정보가 포함됩니다.
		서버에서 결제 내역 검증 요청:
		
		결제 시스템의 서버로 실제 결제가 이루어졌는지 확인하는 요청을 보냅니다. 예를 들어, 카카오페이의 경우 imp_uid나 merchant_uid를 사용하여 결제 내역을 조회하고, 그 응답을 통해 검증을 진행합니다.
		결제 금액 및 상태 확인:
		
		검증 요청에 대한 응답에서 결제 금액, 결제 상태 등을 확인하여 정상 결제인지 여부를 판별합니다. 이 때, 서버에서 기록된 주문 정보와 비교하여 금액이 일치하는지, 결제 상태가 "성공"인지 등을 체크합니다.
		검증 결과 처리:
		
		검증이 완료되면 해당 결과를 바탕으로 결제 완료 처리를 진행하거나, 문제가 있을 경우 사용자에게 오류를 알리고 추가 조치를 취합니다.

		 try {
	            // 1. 결제 정보 조회
	            PaymentInfo paymentInfo = paymentService.getPaymentInfoByImpUid(imp_uid);
	            
	            // 2. 서버에 검증 요청
	            boolean isVerified = paymentService.verifyPayment(imp_uid, merchant_uid, paymentInfo.getAmount());

	            if (isVerified) {
	                // 3. 검증 성공 시 결제 완료 처리
	                paymentService.completePayment(merchant_uid, paymentInfo);

	                return ResponseEntity.ok().body(Map.of("result", "success"));
	            } else {
	                // 검증 실패 시 오류 처리
	                paymentService.removePayAuth(pay_auth_id); // pay_auth 값을 삭제하는 함수

	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("result", "fail", "message", "결제 검증 실패"));
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("result", "error", "message", "서버 오류"));
	        }
	    }
		 */
		
		return ResponseEntity.ok().body(Map.of("result", "success"));
	}
	

}
