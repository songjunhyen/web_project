<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Product List</title>
</head>
<%@ include file="includes/head1.jsp"%>
<style>
#productContainer {
	width: 80%;
	margin: 20px 0;
	text-align: center;
}

#productTable {
	width: 100%;
	border-collapse: collapse;
}

#productTable th, #productTable td {
	border: 1px solid #ddd;
	padding: 8px;
}

#productTable th {
	background-color: #f2f2f2;
	font-weight: bold;
}

#productTable td button {
	background: none;
	border: none;
	color: blue;
	cursor: pointer;
	text-decoration: underline;
}

#pagination {
	margin-top: 20px;
}

#pagination a {
	margin: 0 5px;
	text-decoration: none;
	color: blue;
}
</style>
<body>
	<div id="productContainer">
		<table id="productTable">
			<thead>
				<tr>
					<th></th>
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
						<td>
							<h3>제품 이미지</h3> <c:if test="${not empty product.imageUrl}">
								<c:set var="imageUrls"
									value="${fn:split(product.imageUrl, ',')}" />
								<c:if test="${fn:length(imageUrls) > 0}">
									<img src="${imageUrls[0]}" alt="Product Image"
										style="max-width: 300px;" />
								</c:if>
							</c:if> <c:if test="${empty product.imageUrl}">
								<p>이미지가 없습니다.</p>
							</c:if>
						</td>
						<td>${product.id}</td>
						<td>${product.category}</td>
						<td>
							<form action="/product/Detail" method="post">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
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