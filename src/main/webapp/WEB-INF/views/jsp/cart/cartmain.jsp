<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="com.example.demo.vo.Cart"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장바구니</title>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">


<%
String userId = "";
String cartIds = "";
String sizeColors = "";
int totalPrice = 0;
List<Cart> cartList = (List<Cart>) request.getAttribute("carts");
if (cartList != null && !cartList.isEmpty()) {
    Cart firstCart = cartList.get(0);
    userId = firstCart.getUserid(); // 첫 번째 cart 객체에서 userId를 가져옴
    for (Cart cart : cartList) {
        totalPrice += cart.getPriceall();
        cartIds += cart.getId() + ",";
        sizeColors += cart.getSize() + cart.getColor() + ";";
    }
}
%>

<script>
    var csrfToken;
    var csrfHeader;
    var userid = <%= userId %>;
    var cartIds = [];
    var sizeColors = [];
    var totalPrice =  <%= totalPrice %> ; // JSP에서 JavaScript로 totalPrice 전달

    // 콘솔에 userId 출력
    console.log("User ID:", userid);

    $(document).ready(function() {
        csrfToken = $('meta[name="_csrf"]').attr('content');
        csrfHeader = $('meta[name="_csrf_header"]').attr('content');

        $("#buybutton").click(function(event) {
            event.preventDefault(); // 폼 제출 방지
            buybutton();
        });

        // 카트 아이디와 사이즈/컬러를 준비
        <% 
        StringBuilder cartIdsString = new StringBuilder();
        StringBuilder sizeColorsString = new StringBuilder();
        List<Cart> carts = (List<Cart>) request.getAttribute("carts");
        if (carts != null) {
            for (Cart cart : carts) {
                cartIdsString.append(cart.getId()).append(",");
                sizeColorsString.append(cart.getSize()).append(cart.getColor()).append(";");
            }
        }
        %>
        cartIds = "<%= cartIdsString.toString() %>".split(",").filter(function(id) { return id !== ""; });
        sizeColors = "<%= sizeColorsString.toString() %>".split(";").filter(function(color) { return color !== ""; });
    });

    // 장바구니 목록 초기화 함수
    function loadCartList() {
        $.ajax({
            url: '/Cart/List',
            type: 'GET',
            success: function(response) {
                $('#cartListContainer').html(response); // 장바구니 목록 업데이트
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    }

    // 수정 폼 제출 처리
    function submitModifyForm(form) {
        $.ajax({
            url: form.action,
            type: form.method,
            data: $(form).serialize(),
            success: function(response) {
                // 성공 시 장바구니 목록 갱신
                loadCartList();
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
        return false; // 폼 기본 제출 방지
    }

    // 삭제 폼 제출 처리
    function submitDeleteForm(form) {
        $.ajax({
            url: form.action,
            type: form.method,
            data: $(form).serialize(),
            success: function(response) {
                // 성공 시 장바구니 목록 갱신
                loadCartList();
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
        return false; // 폼 기본 제출 방지
    }

    // 구매하기 버튼 처리
    function buybutton() {
        $.ajax({
            url: '../buying', // 서버에서 결과를 반환하는 URL
            type: 'POST',
            data: {
                userid: userid,
                cartIds: JSON.stringify(cartIds), // 카트 ID 목록
                sizeColors: JSON.stringify(sizeColors), // 사이즈와 컬러 목록
                totalPrice: totalPrice
            },
            beforeSend: function(xhr) {
                // CSRF 토큰을 헤더에 추가
                if (csrfToken && csrfHeader) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            },
            success: function(data) {
                console.log("구매하기 페이지로 이동");
                // 구매 후 페이지 이동 또는 상태 갱신
            },
            error: function(e) {
                console.error("이용 중 오류가 발생했습니다:", e);
            }
        });
    }
</script>
<style>
/* 기본 여백 및 패딩을 제거 */
body {
    margin: 0;
    padding: 0;
    font-family: Arial, sans-serif;
}

/* head와 body 사이 여백을 제거 */
html, body {
    margin: 0;
    padding: 0;
}

/* container 여백 및 패딩을 제거 */
.container {
    margin: 0;
    padding: 0;
    /* 필요에 따라 추가 스타일을 설정할 수 있습니다. */
}

/* 테이블 스타일 */
table {
    width: 100%;
    border-collapse: collapse;
}

th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
}

th {
    background-color: #f4f4f4;
}

/* 버튼 스타일 */
button {
    background-color: #4CAF50;
    color: white;
    border: none;
    padding: 10px 20px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    cursor: pointer;
}

button:hover {
    background-color: #45a049;
}
</style>
</head>
<body>
    <%@ include file="../includes/head1.jsp"%>



	<div class="container">
		<!-- 장바구니 목록을 포함할 영역 -->
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
						<th>수정</th>
						<th>삭제</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="cart" items="${carts}">
						<tr>
							<td>${cart.id}</td>
							<td>${cart.productname}</td>
							<td>${cart.count}</td>
							<td>${cart.priceall}</td>
							<td>${cart.color}</td>
							<td>${cart.size}</td>
							<td>
								<form onsubmit="return submitModifyForm(this)"
									action="/Cart/Modify" method="post">
									<input type="hidden" name="${_csrf.parameterName}"
										value="${_csrf.token}"> <input type="hidden" name="id"
										value="${cart.id}"> <input type="hidden"
										name="productid" value="${cart.productid}"> <input
										type="hidden" name="price" value="${cart.price}"> <input
										type="hidden" name="productname" value="${cart.productname}">
									<input type="hidden" name="a_size" value="${cart.size}">
									<input type="hidden" name="a_color" value="${cart.color}">
									<select name="size">
										<option value="xs">XS</option>
										<option value="s">S</option>
										<option value="m">M</option>
										<option value="l">L</option>
										<option value="xl">XL</option>
									</select> <select name="color">
										<option value="Red">Red</option>
										<option value="Black">Black</option>
										<option value="White">White</option>
										<option value="Blue">Blue</option>
									</select> <input type="number" name="count" step="1" min="1" max="100"
										value="${cart.count}">
									<button type="submit">수정</button>
								</form>
							</td>
							<td>
								<form onsubmit="return submitDeleteForm(this)"
									action="/Cart/Delete" method="post">
									<input type="hidden" name="${_csrf.parameterName}"
										value="${_csrf.token}"> <input type="hidden" name="id"
										value="${cart.id}"> <input type="hidden"
										name="productid" value="${cart.productid}"> <input
										type="hidden" name="size" value="${cart.size}"> <input
										type="hidden" name="color" value="${cart.color}">
									<button type="submit">삭제</button>
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div>
		 금액 : <%= totalPrice %> 원<br>
		<button id = "buybutton">구매하기</button>
		</div>
	</div>


	<%@ include file="../includes/foot1.jsp"%>
</body>
</html>