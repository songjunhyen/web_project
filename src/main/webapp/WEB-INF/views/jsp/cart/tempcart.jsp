<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.example.demo.vo.CartItem"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장바구니 목록</title>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

<%
String sizeColors = "";
String productIds = "";
int totalPrice = 0;
List<CartItem> cartList = (List<CartItem>) request.getAttribute("carts");
if (cartList != null && !cartList.isEmpty()) {
    for (CartItem cart : cartList) {
        totalPrice += cart.getPrice() * cart.getCount();
        sizeColors += cart.getSize() + "-" + cart.getColor() + ";";
        productIds += cart.getProductid() + ";";
    }
}
%>

<script>
var csrfToken;
var csrfHeader;
var sizeColors, productIds;
var totalPrice = <%= totalPrice %>;

$(document).ready(function() {
    csrfToken = $('meta[name="_csrf"]').attr('content');
    csrfHeader = $('meta[name="_csrf_header"]').attr('content');
    
    $("#buybutton").click(function(event) {
        event.preventDefault(); // 폼 제출 방지
        showModal(); // 모달창 열기
    });

    $("#submitEmailPhone").click(function() {
        var email = $("#email").val();
        var phonenum = $("#phonenum").val();
        buybutton(email, phonenum); // 이메일과 전화번호 전달
    });

    $("#closeModal").click(function() {
        hideModal(); // 모달창 닫기
    });

    // 카트 아이디와 사이즈/컬러를 준비
    <% 
    StringBuilder sizeColorsString = new StringBuilder();
    List<CartItem> carts = (List<CartItem>) request.getAttribute("carts");
    if (carts != null) {
        for (CartItem cart : carts) {
            sizeColorsString.append(cart.getSize()).append(cart.getColor()).append(";");
            
        }
    }
    %>
    sizeColors = "<%= sizeColorsString.toString() %>".split(";").filter(function(color) { return color !== ""; });
    productIds = "<%= productIds %>".split(";").filter(function(id) { return id !== ""; });

});

function showModal() {
    $("#emailPhoneModal").show();
}

function hideModal() {
    $("#emailPhoneModal").hide();
}

//구매하기 버튼 처리
function buybutton(email, phonenum) {
    $.ajax({
        url: '../buying', // 서버에서 결과를 반환하는 URL
        type: 'POST',
        data: {
            email: email,
            phonenum: phonenum,
            productIds: JSON.stringify(productIds),
            sizeColors: JSON.stringify(sizeColors), // 사이즈와 컬러 목록
            priceall: totalPrice
        },
        beforeSend: function(xhr) {
            // CSRF 토큰을 헤더에 추가
            if (csrfToken && csrfHeader) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        },
        success: function(data) {
            console.log("구매하기 페이지로 이동");
            hideModal(); // 모달창 닫기
            // 구매 후 페이지 이동 또는 상태 갱신
        },
        error: function(e) {
            console.error("이용 중 오류가 발생했습니다:", e);
        }
    });
}
</script>

<style>
table {
    width: 100%;
    border-collapse: collapse;
}

th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: center;
}

th {
    background-color: #f4f4f4;
}

button {
    padding: 5px 10px;
    margin: 5px;
    cursor: pointer;
}
</style>
</head>
<%@ include file="../includes/head1.jsp"%>
<body>
    <div class="container">
        <div id="cartListContainer">
            <table>
                <thead>
                    <tr>
                        <th>번호</th>
                        <th>제품명</th>
                        <th>수량</th>
                        <th>금액</th>
                        <th>색상</th>
                        <th>사이즈</th>
                        <th>삭제</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cart" items="${carts}">
                        <tr>
                            <td>${cart.productid}</td>
                            <td>${cart.name}</td>
                            <td>
                                <form action="/Temporarily/Cart/Modify" method="post">
                                    <input type="hidden" name="productid" value="${cart.productid}">
                                    <input type="hidden" name="color" value="${cart.color}">
                                    <input type="hidden" name="size" value="${cart.size}">
                                    <input type="number" name="count" step="1" min="1" max="100" value="${cart.count}">
                                    <input type="hidden" name="price" value="${cart.price}">
                                    <button type="submit">수정</button>
                                </form>
                            </td>
                            <td>${cart.price * cart.count}</td>
                            <td>${cart.color}</td>
                            <td>${cart.size}</td>
                            <td>
                                <form action="/Temporarily/Cart/Delete" method="post">
                                    <input type="hidden" name="productid" value="${cart.productid}">
                                    <input type="hidden" name="color" value="${cart.color}">
                                    <input type="hidden" name="size" value="${cart.size}">
                                    <button type="submit">삭제</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

		<div id="emailPhoneModal" style="display: none;">
			<div
				style="position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: white; padding: 20px; border: 1px solid #ddd;">
				<h3>이메일 및 전화번호 입력</h3>
				<label for="email">이메일:</label> <input type="email" id="email"
					name="email" required><br>
				<br> <label for="phonenum">전화번호:</label> <input type="text"
					id="phonenum" name="phonenum" required><br>
				<br>
				<button id="submitEmailPhone">확인</button>
				<button id="closeModal">취소</button>
			</div>
		</div>

		<div>
			금액 : <%= totalPrice %> 원<br>
			<button id="buybutton">구매하기</button>
		</div>
	</div>
	<%@ include file="../includes/foot1.jsp"%>
</body>
</html>