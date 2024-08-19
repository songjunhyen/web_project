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
<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<!-- 네이버페이 -->
<script src="https://nsp.pay.naver.com/sdk/js/naverpay.min.js"></script>
<!-- 토스페이 -->
<script src="https://js.tosspayments.com/v2/standard"></script>

<script>
//IAMPORT 결제 시스템 초기화
var IMP = window.IMP;
IMP.init("가맹점식별코드"); // 가맹점 식별코드 입력

$(document).ready(function() {
    const orderNumber = "${Pinfo != null ? Pinfo.orderNumber : (NPinfo != null ? NPinfo.orderNumber : '')}";
    const price = "${Pinfo != null ? Pinfo.price : (NPinfo != null ? NPinfo.price : '')}";

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
    const orderNumber = "${Pinfo != null ? Pinfo.orderNumber : (NPinfo != null ? NPinfo.orderNumber : '')}";
    const price = "${Pinfo != null ? Pinfo.price : (NPinfo != null ? NPinfo.price : '')}";

    if (orderNumber === "" || price === "") {
        alert("주문번호와 금액 정보가 없습니다. 다시 시도해 주세요.");
        isValid = false;
    }

    return isValid;
}

function naverPay(price, orderNumber) {
    if (confirm("네이버페이로 결제 하시겠습니까?")) {
        $.ajax({
            url: '/initiateNaverPay',
            type: 'POST',
            data: {
                orderNumber: orderNumber,
                price: price
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

function tossPay(price, orderNumber) {
    if (confirm("토스페이로 결제 하시겠습니까?")) {
        $.ajax({
            url: '/initiateTossPay',
            type: 'POST',
            data: {
                orderNumber: orderNumber,
                price: price
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
        url: '/initiateKakaoPay',
        type: 'POST',
        data: {
            orderNumber: orderNumber,
            price: price
        },
        success: function(response) {
            if (response.status === 'success') {
                let paymentData = response.paymentData;
                IMP.request_pay({
                    pg: paymentData.pg,
                    pay_method: paymentData.pay_method,
                    merchant_uid: paymentData.merchant_uid,
                    name: paymentData.name,
                    amount: paymentData.amount
                }, function (rsp) {
                    if (rsp.success) {
                        completePurchase({
                            orderNumber: orderNumber,
                            price: price,
                            phone: $('#phone').val(),
                            address: $('#address').val(),
                            paymentMethod: 'kakaopay',
                            merchant_uid: paymentData.merchant_uid
                        });
                    } else {
                        alert(rsp.error_msg);
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

function completePurchase(data) {
    $.ajax({
        url: '/completePurchase',
        type: 'POST',
        data: {
            orderNumber: data.orderNumber,
            price: data.price,
            phone: data.phone,
            address: data.address,
            paymentMethod: data.paymentMethod,
            merchant_uid: data.merchant_uid || '' // merchant_uid가 없는 경우 빈 문자열로 처리
        },
        success: function(response) {
            if (response.status === 200) {
                if (data.redirectUrl) {
                    window.location.href = data.redirectUrl;
                } else {
                    alert('결제 완료!');
                    window.location.reload();
                }
            } else {
                alert(`결제 완료 요청 실패: [${response.status}] ${response.message}`);
            }
        },
        error: function() {
            alert('결제 완료 후 DB 저장 중 오류가 발생했습니다.');
        }
    });
}
</script>

</head>
<body>
	<h1>구매 정보</h1>

	<!-- 회원 구매 정보 -->
	<c:if test="${not empty Pinfo}">
		<h2>회원 구매 정보</h2>
		<p>주문 ID: ${Pinfo.orderNumber}</p>
		<p>제품 ID: ${Pinfo.productid}</p>
		<p>제품명: ${Pinfo.productname}</p>
		<p>사이즈와 색상: ${Pinfo.sizecolor}</p>
		<p>가격: ${Pinfo.price} 원</p>
	</c:if>

	<!-- 비회원 구매 정보 -->
	<c:if test="${not empty NPinfo}">
		<h2>비회원 구매 정보</h2>
		<p>주문 ID: ${NPinfo.orderNumber}</p>
		<p>이메일: ${NPinfo.email}</p>
		<p>전화번호: ${NPinfo.phonenum}</p>
		<p>제품 ID: ${NPinfo.productid}</p>
		<p>제품명: ${NPinfo.productname}</p>
		<p>사이즈와 색상: ${NPinfo.sizecolor}</p>
		<p>가격: ${NPinfo.price} 원</p>
	</c:if>

	<form id="purchaseForm" action="/completePurchase" method="post">
		<h2>추가 정보 입력</h2>

		<!-- 전화번호 입력 필드 -->
		<c:choose>
			<c:when test="${Pinfo.phone eq '' or NPinfo.phonenum eq ''}">
				<label for="phone">전화번호:</label>
				<input type="text" id="phone" name="phone" class="required" />
				<br>
			</c:when>
			<c:otherwise>
				<input type="hidden" id="phone" name="phone"
					value="${not empty Pinfo.phone ? Pinfo.phone : NPinfo.phonenum}" />
			</c:otherwise>
		</c:choose>

		<!-- 주소 입력 필드 -->
		<c:choose>
			<c:when test="${Pinfo.address eq '' or NPinfo.address eq ''}">
				<label for="address">주소:</label>
				<input type="text" id="address" name="address" class="required" />
				<br>
			</c:when>
			<c:otherwise>
				<input type="hidden" id="address" name="address"
					value="${not empty Pinfo.address ? Pinfo.address : NPinfo.address}" />
			</c:otherwise>
		</c:choose>

		<button type="button" id="confirmBtn1">카카오페이로 결제하기</button>
		<br>
		<button type="button" id="confirmBtn2">네이버페이로 결제하기</button>
		<br>
		<button type="button" id="confirmBtn3">토스페이로 결제하기</button>
		<br>
	</form>
</body>
</html>