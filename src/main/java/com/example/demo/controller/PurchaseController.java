package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.PurchaseService;
import com.example.demo.util.SecurityUtils;
import com.example.demo.vo.NonMemberPurchaseInfo;
import com.example.demo.vo.PurchaseInfo;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PurchaseController {

	@Autowired
	private PurchaseService purchaseService;

	@PostMapping("/buying")
	public void processPurchase(@RequestParam(required = false) String userid,
			@RequestParam(required = false) String email, @RequestParam(required = false) String phonenum,
			@RequestParam(required = false) String productid, @RequestParam(required = false) String productname,
			@RequestParam(required = false) String sizecolor, @RequestParam(required = false) List<String> productIds,
			@RequestParam(required = false) List<String> cartIds,
			@RequestParam(required = false) List<String> sizeColors, @RequestParam(required = false) Integer priceall,
			HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {

		LocalDateTime now = LocalDateTime.now();

		String cartIdsString = cartIds != null ? String.join(",", cartIds) : "";
		String sizeColorsString = sizeColors != null ? String.join(";", sizeColors) : "";
		String productIdsString = productIds != null ? String.join(",", productIds) : "";

		if (email != null && phonenum != null) {
			// 비회원 처리
			NonMemberPurchaseInfo NPinfo = new NonMemberPurchaseInfo();
			NPinfo.setEmail(email);
			NPinfo.setPhonenum(phonenum);
			if (productid != null && !productid.isEmpty()) {
				NPinfo.setProductid(productid);
			} else {
				NPinfo.setProductid(productIdsString);
			}
			NPinfo.setProductname(productname);
			NPinfo.setSizecolor(sizecolor);
			NPinfo.setPrice(priceall);
			NPinfo.setRequestDate(now); // 요청 일자 설정
			purchaseService.nonmemreqPurchase(NPinfo);
			redirectAttributes.addFlashAttribute("NPinfo", NPinfo);

		} else {
			// 회원 처리
			PurchaseInfo Pinfo = new PurchaseInfo();
			if (userid != null) {
				Pinfo.setUserid(userid);
			} else {
				userid = SecurityUtils.getCurrentUserId();
				Pinfo.setUserid(userid);
			}
			if (cartIds != null && !cartIds.isEmpty()) {
				Pinfo.setCartids(cartIdsString);
			} else {
				Pinfo.setCartids("");
			}
			if (productid != null) {
				Pinfo.setProductid(Integer.parseInt(productid));
			}
			Pinfo.setProductname(productname);
			if (sizecolor != null && !sizecolor.isEmpty()) {
				Pinfo.setSizecolor(sizecolor);
			} else {
				Pinfo.setSizecolor(sizeColorsString);
			}
			Pinfo.setPrice(priceall);
			Pinfo.setRequestDate(now); // 요청 일자 설정
			purchaseService.requestPurchase(Pinfo);
			redirectAttributes.addFlashAttribute("Pinfo", Pinfo);
		}

		// 리다이렉트 처리
		response.sendRedirect("/confirmation");
	}

	@PostMapping("/validatePurchase") // 구매 시도 시 호출
	public void paymentPurchase(@RequestParam String orderNumber, @RequestParam int price, @RequestParam String phone,
			@RequestParam String address, @RequestParam String paymentMethod) {

		LocalDateTime now = LocalDateTime.now();

		// 오더 넘버로 이미 DB에 결제정보 있는지 검증(중복결제 방지)
		// 결제 상태도 저장하도록 그래야 DB에 있어도 결제상태로 결제전이라 되어있으면 괜찮고

		// 결제 정보를 저장하는 로직
		// 예: 결제 정보 저장, 주문 상태 업데이트 등
		// savePaymentInfo(orderNumber, price, phone, address, paymentMethod);

		// 결제 정보 검증:
		// orderNumber를 사용하여 DB에서 이미 결제 정보가 존재하는지 확인합니다. 이를 통해 중복 결제 방지와 같은 검증 작업을
		// 수행합니다.
		// 검증 로직에 따라 orderNumber로 DB에서 해당 주문의 상태나 결제 정보를 조회하고, 필요한 유효성 검사를 진행합니다.

		// 검증 결과 반환:
		// 검증 결과에 따라 적절한 응답을 반환합니다.
		// 예를 들어, 검증이 성공했으면 성공 상태(예: HTTP 200 OK)와 함께 필요한 정보를 반환하고, 검증 실패 시 적절한 에러 메시지와
		// 상태 코드를 반환합니다.

	}

}