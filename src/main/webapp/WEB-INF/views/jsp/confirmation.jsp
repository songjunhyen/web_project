<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>구매 정보 추가입력</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- iamport.js -->
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
<!-- 네이버페이 -->
<script src="https://nsp.pay.naver.com/sdk/js/naverpay.min.js"></script>
<!-- 토스페이 -->
<script src="https://js.tosspayments.com/v2/standard"></script>

<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">


<script>
var csrfToken;
var csrfHeader;

var IMP = window.IMP;
IMP.init("imp30108185"); // 가맹점 식별코드 입력

$(document).ready(function() {
    csrfToken = $('meta[name="_csrf"]').attr('content');
    csrfHeader = $('meta[name="_csrf_header"]').attr('content');    
    
    const orderNumber = "${purchaseInfo.Pinfo != null ? purchaseInfo.Pinfo.orderNumber : (purchaseInfo.NPinfo != null ? purchaseInfo.NPinfo.orderNumber : '')}";
    const price = "${purchaseInfo.Pinfo != null ? purchaseInfo.Pinfo.price : (purchaseInfo.NPinfo != null ? purchaseInfo.NPinfo.price : '')}";
    
    console.log("orderNumber:", orderNumber);
    console.log("price:", price);

    $("#confirmBtn1").on("click", function() {
        handlePayment('kakaopay', price, orderNumber);
    });

    $("#confirmBtn2").on("click", function() {
        handlePayment('naverpay', price, orderNumber);
    });

    $("#confirmBtn3").on("click", function() {
        handlePayment('toss', price, orderNumber);
    });
});

function handlePayment(paymentMethod, price, orderNumber) {
    if (validateInputs()) {
        $.ajax({
            url: '/validatePurchase',
            type: 'POST',
            data: {
                orderNumber: orderNumber,
                price: price,
                phone: $('#phone').val(),
                address: $('#address').val(),
                paymentMethod: paymentMethod
            },
            beforeSend: function(xhr) {
                // CSRF 토큰을 헤더에 추가
                if (csrfToken && csrfHeader) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            },
            success: function(validateResponse) {
                if (validateResponse.status == 200) {
                    if (paymentMethod === 'kakaopay') {
                        initiateKakaoPay(price, orderNumber);
                    } else if (paymentMethod === 'naverpay') {
                        naverPay(price, orderNumber);
                    } else if (paymentMethod === 'toss') {
                        tossPay(price, orderNumber);
                    }
                } else {
                    alert(`검증 실패: [${validateResponse.status}] ${validateResponse.message}`);
                }
            },
            error: function() {
                alert('검증 요청 중 오류가 발생했습니다.');
            }
        });
    }
}

function validateInputs() {
    let isValid = true;

    $(".required").each(function() {
        if ($(this).val().trim() === "") {
            alert("필수 입력 필드를 모두 채워주세요.");
            isValid = false;
            return false; // Loop break
        }
    });

    // Changed to use arguments
    const orderNumber = "${purchaseInfo.Pinfo != null ? purchaseInfo.Pinfo.orderNumber : (purchaseInfo.NPinfo != null ? purchaseInfo.NPinfo.orderNumber : '')}";
    const price = "${purchaseInfo.Pinfo != null ? purchaseInfo.Pinfo.price : (purchaseInfo.NPinfo != null ? purchaseInfo.NPinfo.price : '')}";
        
    if (orderNumber === "" || price === "") {
        alert("주문번호와 금액 정보가 없습니다. 다시 시도해 주세요.");
        isValid = false;
    }

    return isValid;
}

function naverPay(price, orderNumber) {
	
	//<input type="button" id="naverPayBtn" value="네이버페이 결제 버튼">
	//<script src="https://nsp.pay.naver.com/sdk/js/naverpay.min.js"> <스크립트닫기>
	
    //var oPay = Naver.Pay.create({
    //      "mode" : "{#_mode}", // development or production
    //      "clientId": "{#_clientId}", // clientId
    //      "chainId": "{#_chainId}" // chainId
    //});

    //직접 만드신 네이버페이 결제버튼에 click Event를 할당하세요
    //var elNaverPayBtn = document.getElementById("naverPayBtn");

    //elNaverPayBtn.addEventListener("click", function() {
    //    oPay.open({
    //      "merchantUserKey": "{#_merchantUserKey}",
    //      "merchantPayKey": "{#_merchantPayKey}",
    //      "productName": "{#_productName}",
    //      "totalPayAmount": "{#_totalPayAmount}",
    //      "taxScopeAmount": "{#_taxScopeAmount}",
    //      "taxExScopeAmount": "{#_taxExScopeAmount}",
    //      "returnUrl": "{#_returnUrl}"
    //    });
    //});
    
    // 결제 승인
    //curl -X POST https://dev.apis.naver.com/naverpay-partner/naverpay/payments/v2.2/apply/payment \
	//-H X-Naver-Client-Id:{발급된 client id} \
	//-H X-Naver-Client-Secret:{발급된 client secret} \
	//-H X-NaverPay-Chain-Id:{발급된 chain id} \
	//-H X-NaverPay-Idempotency-Key: {API 멱등성 키} \
	//-d paymentId={네이버페이가 발급한 결제 번호} \
	
	/*
	결제 완료시
	    "code" : "Success",
    "message": "detail message(optional)",
    "body": {
        "paymentId": "20170201NP1043587746",
        "detail": {
               "productName": "상품명",
               "merchantId": "loginId",
               "merchantName": "가맹점명",
               "cardNo": "465887**********",
               "admissionYmdt": "20170201151722",
               "payHistId": "20170201NP1043587781",
               "totalPayAmount": 1000,
               "primaryPayAmount": 1000,
               "npointPayAmount": 0,
               "giftCardAmount": 0,
               "taxScopeAmount": 1000,
               "taxExScopeAmount": 0,
               "environmentDepositAmount": 0,
               "primaryPayMeans": "CARD",
               "merchantPayKey": "order-key",
               "merchantUserKey": "jenie",
               "cardCorpCode": "C0",
               "paymentId": "20170201NP1043587746",
               "admissionTypeCode": "01",
               "settleExpectAmount": 971,
               "payCommissionAmount": 29,
               "admissionState": "SUCCESS",
               "tradeConfirmYmdt": "20170201152510",
               "cardAuthNo": "17545616",
               "cardInstCount": 0,
               "usedCardPoint": false,
               "bankCorpCode": "",
               "bankAccountNo": "",
               "settleExpected": false,
               "extraDeduction": false,
               "useCfmYmdt" : "20180703"
        }
    }	
	*/
	
    if (confirm("네이버페이로 결제 하시겠습니까?")) {
        $.ajax({
            url: '/initiateNaverPay',
            type: 'POST',
            data: {
                orderNumber: orderNumber,
                price: price
            },
            beforeSend: function(xhr) {
                // CSRF 토큰을 헤더에 추가
                if (csrfToken && csrfHeader) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            },
            success: function(response) {
                if (response.status === 'success') {
                    let redirectUrl = response.redirectUrl;

                    completePurchase({
                        orderNumber: orderNumber,
                        price: price,
                        phone: $('#phone').val(),
                        address: $('#address').val(),
                        paymentMethod: 'naverpay',
                        redirectUrl: redirectUrl
                    });
                } else {
                    alert(`결제 요청 실패: [${response.status}] ${response.message}`);
                }
            },
            error: function() {
                alert('결제 요청 중 오류가 발생했습니다.');
            }
        });
    }
}

function tossPay(price, orderNumber) {  // 토스 결제 생성 API Endpoint POST : https://pay.toss.im/api/v2/payments
	//결제 생성'을 위해 9가지 설정값
	//"orderNo":"1",
	//"amount":25000,
	//"amountTaxFree":0,
	//"productDesc":"토스티셔츠",
	//"apiKey":"sk_test_w5lNQylNqa5lNQe013Nq",
	//"autoExecute":true,   //결제 생성 시, autoExecute를 'false'로 설정 : 해당 결제건은 가맹점의 최종 승인 전까진 대기 상태
	//"resultCallback":"https://YOUR-SITE.COM/callback",
	//"retUrl":"http://YOUR-SITE.COM/ORDER-CHECK",
	//"retCancelUrl":"http://YOUR-SITE.COM/close"
	
	//승인 : 구매자 인증이 완료된 '대기' 상태에서 재고상태 확인 후 결제를 진행
	//curl "https://pay.toss.im/api/v2/execute" \
	//"apiKey":"sk_test_w5lNQylNqa5lNQe013Nq", # 상점의 API Key (필수)
	//"payToken":"example-payToken",           # 결제 고유 번호 (필수)

	//결제 상태
	//curl "https://pay.toss.im/api/v2/status" \
	//"apiKey":"sk_test_w5lNQylNqa5lNQe013Nq",   # 상점의 API Key (필수)
	//"payToken":"example-payToken",             # 결제 고유 번호 (필수)
	//결제상태 응답
	//	"code": 0,                            # 응답코드
	//	"payToken": "example-payToken",       # 결제 고유 토큰
	//	"orderNo": "1",                       # 상점의 주문번호
		//"payStatus": "PAY_COMPLETE",          # 결제 상태
	//	"payMethod": "TOSS_MONEY",            # 결제 수단
	//	"amount": 15000,                      # 결제 요청금액
	//	"transactions": [                     # 거래 트랜잭션 목록
	//	 {
	//	    "stepType": "PAY",
	//	    "transactionId": "3243c76e-4669-881b-33a3b82ddf49",
	//	    "transactionAmount": 15000,
	//    "discountAmount": 300,
	//    "pointAmount": 0,
	//    "paidAmount": 14700,
	//    "regTs": "2020-03-01 12:33:20"
	// 	}
	//	],
	//	"createdTs": "2020-03-01 12:33:04",   # 최초 결제요청 시간
	//	"paidTs": "2020-03-01 12:33:20"       # 결제 완료 처리 시간

	
	
    if (confirm("토스페이로 결제 하시겠습니까?")) {
        $.ajax({
            url: '/initiateTossPay',
            type: 'POST',
            data: {
                orderNumber: orderNumber,
                price: price
            },
            beforeSend: function(xhr) {
                // CSRF 토큰을 헤더에 추가
                if (csrfToken && csrfHeader) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            },
            success: function(response) {
                if (response.status === 'success') {
                    let redirectUrl = response.redirectUrl;

                    completePurchase({
                        orderNumber: orderNumber,
                        price: price,
                        phone: $('#phone').val(),
                        address: $('#address').val(),
                        paymentMethod: 'toss',
                        redirectUrl: redirectUrl
                    });
                } else {
                    alert(`결제 요청 실패: [${response.status}] ${response.message}`);
                }
            },
            error: function() {
                alert('결제 요청 중 오류가 발생했습니다.');
            }
        });
    }
}

function initiateKakaoPay(price, orderNumber) {
    $.ajax({
        url: 'initiateKakaoPay',
        type: 'POST',
        data: {
            orderNumber: orderNumber,
            price: price
        },
        beforeSend: function(xhr) {
            // CSRF 토큰을 헤더에 추가
            if (csrfToken && csrfHeader) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        },
        success: function(response) {
            if (response.status === 'success') {
                let paymentData = response.paymentData;
                const { merchant_uid, pay_auth_id, amount } = paymentData;

                console.log("Payment Data:", paymentData);	
                
                // 카카오페이 결제 요청
                IMP.request_pay(
                		{
                    pg: "kakao", // 카카오페이
                    pay_method: "card",
                    merchant_uid: merchant_uid,
                    name: paymentData.name, // 결제창에서 보여질 이름
                    amount: amount, // 가격
                    buyer_name: $('#name').val(), // 구매자 이름
                    buyer_tel: $('#phone').val(), // 구매자 전화번호
                }, function (rsp) { 
                    if (rsp.success) {
                        // 결제 성공 시 서버에 결과 전송
                        $.ajax({
                            url: '/pay/complete',
                            type: 'POST',
                            data: {
                                imp_uid: rsp.imp_uid,
                                merchant_uid: rsp.merchant_uid,
                                pay_auth_id: pay_auth_id,
                                orderNumber: orderNumber
                            },
                            success: function(data) {
                                if (data.result.code != 200) {
                                    // 결제 실패
                                    alert("위조된 결제 시도에 의해 결제에 실패했습니다.");
                                    // pay_auth 값을 지우는 함수 호출
                                    removePayAuth(pay_auth_id); //구현해야됨
                                } else {
                                    // 결제 성공
                                    alert("결제에 성공했습니다.");
                                    window.location.reload(); // 페이지 새로고침 => 결제완료 페이지로 가도록 변경 예정
                                }
                            },
                            error: function() {
                                alert('결제 완료 후 DB 저장 중 오류가 발생했습니다.');
                            }
                        });
                    } else {
                        // 결제 실패
                        removePayAuth(pay_auth_id); // pay_auth 값을 지우는 함수 호출
                        alert("결제에 실패했습니다. : " + rsp.error_msg);
                    }
                });
            } else {
                alert(`결제 요청 실패: [${response.status}] ${response.message}`);
            }
        },
        error: function() {
            alert('결제 요청 중 오류가 발생했습니다.');
        }
    });
}

</script>

</head>
<body>
	<h1>구매 정보</h1>
		
	보여줄거
	구매정보
	주문 번호
	요청일
	제품명
	갯수 합친것(총 상품수)
	금액
	
	배송 정보 입력
	받는 분 연락처(무조건 입력하도록)
	주소 얘도 입력
	
	배송정보 가져오기 버튼
	(회원은 그냥 작은 창나오게
	비회원은 연락처 이메일 입력해야 나오게
	작은창에는 이전 배송건 회원의 경우는 저장된 주소랑 같이)
	가져오기 누르면 입력필드에 자동으로 들어가도록	

	
	갯수부분은 따로 합해서 count로
	컨트롤러에서 갯수 합친걸로 하도록 변경 카트->구매하기 면 count는0이고 사이즈색상에 합쳐놨으니 분해해서 쓰면되고
	제품명은 장바구니 아이디나 제품id로 검색하도록 제품id가 0이면 cartid로 검색하도록하면 될긋	


	<!-- 회원 구매 정보 -->
    <c:if test="${not empty sessionScope.purchaseInfo}">
        <c:set var="purchaseInfo" value="${sessionScope.purchaseInfo}" />
        
        <c:choose>
            <c:when test="${not empty purchaseInfo.Pinfo}">
                <h2>회원 구매 정보</h2>
		        <p>주문 ID: ${purchaseInfo.Pinfo.orderNumber}</p>
		        <p>제품 ID: ${purchaseInfo.Pinfo.productid}</p>
		        <p>장바구니 ID: ${purchaseInfo.Pinfo.cartids}</p>
		        <p>제품명: ${purchaseInfo.Pinfo.productname}</p>
		        <p>사이즈와 색상: ${purchaseInfo.Pinfo.sizecolor}</p>
		        <p>가격: ${purchaseInfo.Pinfo.price} 원</p>
		        <p>수량: ${purchaseInfo.Pinfo.quantity}</p>
		        <p>전화번호: ${purchaseInfo.Pinfo.phone}</p>
		        <p>주소: ${purchaseInfo.Pinfo.address}</p>
		        <p>요청일: ${purchaseInfo.Pinfo.requestDate}</p>
            </c:when>
            <c:otherwise>
                <h2>비회원 구매 정보</h2>
                <p>주문 ID: ${purchaseInfo.NPinfo.orderNumber}</p>
                <p>이메일: ${purchaseInfo.NPinfo.email}</p>
                <p>전화번호: ${purchaseInfo.NPinfo.phonenum}</p>
                <p>제품 ID: ${purchaseInfo.NPinfo.productid}</p>
                <p>제품명: ${purchaseInfo.NPinfo.productname}</p>
                <p>사이즈와 색상: ${purchaseInfo.NPinfo.sizecolor}</p>
                <p>가격: ${purchaseInfo.NPinfo.price} 원</p>
            </c:otherwise>
        </c:choose>
    </c:if>

	<form id="purchaseForm" action="/completePurchase" method="post">
		<h2>추가 정보 입력</h2>		
		<br>
		<!-- 전화번호 입력 필드 -->
		<c:choose>
			<c:when test="${purchaseInfo.Pinfo.phone eq '' or purchaseInfo.NPinfo.phonenum eq ''}">
				<label for="phone">전화번호:</label>
				<input type="text" id="phone" name="phone" class="required" />
				<br>
			</c:when>
			<c:otherwise>
				<input type="hidden" id="phone" name="phone"
					value="${not empty purchaseInfo.Pinfo.phone ? purchaseInfo.Pinfo.phone : purchaseInfo.NPinfo.phonenum}" />
					a
			</c:otherwise>
		</c:choose>
		<br>
		<!-- 주소 입력 필드 -->
		<c:choose>
			<c:when test="${purchaseInfo.Pinfo.address eq '' or purchaseInfo.NPinfo.address eq ''}">
				<label for="address">주소:</label>
				<input type="text" id="address" name="address" class="required" />
				<br>
			</c:when>
			<c:otherwise>
				<input type="hidden" id="address" name="address"
					value="${not empty purchaseInfo.Pinfo.address ? purchaseInfo.Pinfo.address : purchaseInfo.NPinfo.address}" />
					a
			</c:otherwise>
		</c:choose>
		<br>
		<button type="button" id="confirmBtn1">카카오페이로 결제하기</button>
		<br>
		<button type="button" id="confirmBtn2">네이버페이로 결제하기</button>
		<br>
		<button type="button" id="confirmBtn3">토스페이로 결제하기</button>
		<br>
	</form>
</body>
</html>