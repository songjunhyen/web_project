<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장바구니</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	//페이지 로드 시 초기 데이터 로드
	$(document).ready(function() {
		// 초기 장바구니 목록 로드
		loadCartList();
	});

	// 장바구니 목록 초기화 함수
	function loadCartList() {
		$('#cartListContainer').load('/Cart/List');
	}

	// 수정 폼 제출 처리
	function submitModifyForm(form) {
		$.ajax({
			url : form.action,
			type : form.method,
			data : $(form).serialize(),
			success : function(response) {
				// 성공 시 장바구니 목록 갱신
				loadCartList();
			},
			error : function(error) {
				console.error('Error:', error);
			}
		});
		return false; // 폼 기본 제출 방지
	}

	// 삭제 폼 제출 처리
	function submitDeleteForm(form) {
		$.ajax({
			url : form.action,
			type : form.method,
			data : $(form).serialize(),
			success : function(response) {
				// 성공 시 장바구니 목록 갱신
				loadCartList();
			},
			error : function(error) {
				console.error('Error:', error);
			}
		});
		return false; // 폼 기본 제출 방지
	}
</script>
</head>
<body>
	<div class="container">
		<h1>장바구니</h1>

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
							<td>
								<form onsubmit="return submitModifyForm(this)"
									action="/Cart/Modify" method="post">
									<input type="hidden" name="productId" value="${cart.productid}">
									<input type="number" name="count" step="1" min="1"
										max="${cart.count}" value="${cart.count}">
									<button type="submit">수정</button>
								</form>
							</td>
							<td>${cart.price}</td>
							<td>${cart.color}</td>
							<td>${cart.size}</td>
							<td>
								<form onsubmit="return submitDeleteForm(this)"
									action="/Cart/Delete" method="post">
									<input type="hidden" name="productId" value="${cart.productid}">
									<button type="submit">삭제</button>
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

	</div>
</body>
</html>