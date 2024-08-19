package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.PurchaseService;
import com.example.demo.util.SecurityUtils;
import com.example.demo.vo.NonMemberPurchaseInfo;
import com.example.demo.vo.PaymentInfo;
import com.example.demo.vo.PurchaseInfo;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class PurchaseController {

	@Autowired
	private PurchaseService purchaseService;

	@PostMapping("/buying")
	public ResponseEntity<Map<String, Object>> processPurchase(@RequestParam(required = false) String userid,
			@RequestParam(required = false) String email, @RequestParam(required = false) String phonenum,
			@RequestParam(required = false) String productid, @RequestParam(required = false) String productname,
			@RequestParam(required = false) String sizecolor, @RequestParam(required = false) List<String> productIds,
			@RequestParam(required = false) List<String> cartIds,
			@RequestParam(required = false) List<String> sizeColors, @RequestParam(required = false) Integer priceall,
	        HttpSession session) {

		LocalDateTime now = LocalDateTime.now();
		Map<String, Object> response = new HashMap<>();

		String cartIdsString = cartIds != null ? String.join(",", cartIds) : "";
		String sizeColorsString = sizeColors != null ? String.join(";", sizeColors) : "";
		String productIdsString = productIds != null ? String.join(",", productIds) : "";

		if (email != null && phonenum != null) {
			// 비회원 처리
			NonMemberPurchaseInfo NPinfo = new NonMemberPurchaseInfo();
			NPinfo.setEmail(email);
			NPinfo.setPhonenum(phonenum);
			NPinfo.setProductid(productid != null && !productid.isEmpty() ? productid : productIdsString);
			NPinfo.setProductname(productname);
			NPinfo.setSizecolor(sizecolor);
			NPinfo.setPrice(priceall);
			NPinfo.setRequestDate(now);
			purchaseService.nonmemreqPurchase(NPinfo);
			response.put("NPinfo", NPinfo);

		} else {
			// 회원 처리
			PurchaseInfo Pinfo = new PurchaseInfo();
			Pinfo.setUserid(userid != null ? userid : SecurityUtils.getCurrentUserId());
			Pinfo.setCartids(cartIds != null && !cartIds.isEmpty() ? cartIdsString : "");
			if (productid != null) {
				Pinfo.setProductid(Integer.parseInt(productid));
			}
			Pinfo.setProductname(productname);
			Pinfo.setSizecolor(sizecolor != null && !sizecolor.isEmpty() ? sizecolor : sizeColorsString);
			Pinfo.setPrice(priceall);
			Pinfo.setRequestDate(now);
			purchaseService.requestPurchase(Pinfo);
			response.put("Pinfo", Pinfo);
		}

		session.setAttribute("purchaseInfo", response);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/confirmation")
	public String showConfirmationPage(Model model) {

		// ((RedirectAttributes) model).addAttribute("additionalMessage", "Thank you for
		// your purchase!");

		return "confirmation"; // JSP 파일 이름
	}

	@PostMapping("/validatePurchase")
	public ResponseEntity<Map<String, Object>> validatePurchase(
	        @RequestParam String orderNumber,
	        @RequestParam int price,
	        @RequestParam String phone,
	        @RequestParam String address,
	        @RequestParam String paymentMethod) {

	    Map<String, Object> response = new HashMap<>();
		Object orderInfo = null;
		
	    try {
	        // 데이터베이스에서 주문 정보를 조회합니다.
	    	String PN = purchaseService.Searchis(orderNumber);
	        
	        if (PN.equals("Member")) {
	            orderInfo = purchaseService.getOrderInfoByPInfo(orderNumber);
	        } else if (PN.equals("Nonmember")) {
	            orderInfo = purchaseService.getOrderInfoByNInfo(orderNumber);
	        } else {
	            response.put("status", "error");
	            response.put("message", "잘못된 주문 정보 타입입니다.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }

	        // 주문 정보가 존재하는지 확인합니다.
	        if (orderInfo == null) {
	            response.put("status", "error");
	            response.put("message", "주문 정보를 찾을 수 없습니다.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }
	        
	        // 이미 결제 정보가 존재하는지 확인합니다.
	        String paymentStatus = purchaseService.getPaymentStatus(orderNumber);
	        if (paymentStatus != null &&  paymentStatus.equals("completed")) {
	            response.put("status", "error");
	            response.put("message", "이미 결제가 완료된 주문입니다.");
	            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	        }

	        // 결제 정보 저장 또는 업데이트
	        PaymentInfo paymentInfo = new PaymentInfo();
	        paymentInfo.setOrderNumber(orderNumber);
	        paymentInfo.setPrice(price);
	        paymentInfo.setPhone(phone);
	        paymentInfo.setAddress(address);
	        paymentInfo.setPaymentMethod(paymentMethod);
	        paymentInfo.setPaymentStatus("pending"); // 결제 대기 상태로 설정
	        paymentInfo.setPaymentDate(LocalDateTime.now());
	        
	        purchaseService.saveupPaymentInfo(paymentInfo);

	        // 검증이 성공하면 성공 상태를 반환합니다.
	        response.put("status", "success");
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        // 예외 발생 시 실패 응답을 반환합니다.
	        response.put("status", "error");
	        response.put("message", "검증 요청 처리 중 오류가 발생했습니다.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

}