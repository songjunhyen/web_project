<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장바구니</title>

<%-- include  head에 흠오르 링크 있고 그걸로 나오도록 --%>


<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>

	// 장바구니 목록 초기화 함수
	function loadCartList() {
		$.ajax({
			url : '/Cart/List',
			type : 'GET',
			success : function(response) {
				$('#cartListContainer').html(response); // 장바구니 목록 업데이트
			},
			error : function(error) {
				console.error('Error:', error);
			}
		});
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
<%@ include file="../includes/head1.jsp"%>
<body>
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
						<th></th>
						<th></th>
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
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
									<input type="hidden" name="id" value="${cart.id}">	
									<input type="hidden" name="productid" value="${cart.productid}">	
								    <input type="hidden" name="price" value="${cart.price}">			
									<input type="hidden" name="productname" value="${cart.productname}">		
									<input type="hidden" name="a_size" value="${cart.size}">
									<input type="hidden" name="a_color" value="${cart.color}">						
									<select name="size">
										<option value="xs">XS</option>
										<option value="s">S</option>
										<option value="m">M</option>
										<option value="l">L</option>
										<option value="xl">XL</option>
									</select> 
									<select name="color">
										<option value="Red">Red</option>
										<option value="Black">Black</option>
										<option value="White">White</option>
										<option value="Blue">Blue</option>
									</select> 
									<input type="number" name="count" step="1" min="1" max="100" value="${cart.count}">
									<button type="submit">수정</button>
								</form>
							</td>
							<td>
								<form onsubmit="return submitDeleteForm(this)"
									action="/Cart/Delete" method="post">
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
									<input type="hidden" name="id" value="${cart.id}">	
									<input type="hidden" name="productid" value="${cart.productid}">
									<input type="hidden" name="size" value="${cart.size}">
									<input type="hidden" name="color" value="${cart.color}">
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