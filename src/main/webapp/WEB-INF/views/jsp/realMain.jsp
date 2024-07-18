<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="includes/head1.jsp"%>
<meta charset="UTF-8">
<title>Product List</title>
</head>
<body>
	<div id="productContainer">
		<table id="productTable">
			<thead>
				<tr>
					<th>번호</th>
					<th>카테고리</th>
					<th>제품명</th>
					<th>금액</th>
					<th>조회수</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="product" items="${products}">
					<tr>
						<td>${product.id}</td>
						<td>${product.category}</td>
						<td>
							<form action="/product/Detail" method="post">
								<input type="hidden" name="id" value="${product.id}">
								<button type="submit">${product.name}</button>
							</form>
						</td>
						<td>${product.price}</td>
						<td>${product.viewcount}</td>
						<td>${product.regDate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<%-- 페이징 버튼 --%>
	<div>
	    <%-- 이전 페이지 --%>
	    <c:if test="${currentPage > 1}">
	        <a href="?page=${currentPage - 1}">이전</a>
	    </c:if>

	    <%-- 다음 페이지 --%>
	    <c:if test="${currentPage < totalPages}">
	        <a href="?page=${currentPage + 1}">다음</a>
	    </c:if>
	</div>
	
	<%@ include file="includes/foot1.jsp"%>
</body>
</html>