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
#main {
	
}

#productContainer {
	width: 80%;
	margin: 30px auto;
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

#pagingContainer {
	text-align: right; /* 페이징 버튼을 우측으로 정렬 */
	margin-top: 20px; /* 위쪽 여백 추가 */
	margin: 0 2px; 
}

#pagingContainer a, #pagingContainer span {
	width: 20px;
	display: inline-block;
	margin: 0 4px; /* 각 페이지 번호 사이에 여백 추가 */
	padding: 3px 1px; /* 패딩 추가 */
	text-decoration: none; /* 링크의 밑줄 제거 */
	color: black; /* 텍스트 색상 설정 */
	border: 1px solid #ddd; /* 테두리 추가 */
	border-radius: 5px; /* 둥근 모서리 추가 */
	text-align: center;
}

#pagingContainer #pagingbotton {
	width: 35px; /* 너비 조정 */
}

#pagingContainer a:hover {
	background-color: #ddd; /* 마우스 오버 시 배경색 변경 */
}

#pagingContainer span {
	font-weight: bold; /* 현재 페이지 번호 강조 */
}
</style>
<body id="main">
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
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}"> <input type="hidden" name="id"
									value="${product.id}">
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
	<div id="pagingContainer">
        <!-- 현재 페이지와 총 페이지 수 -->
        <c:if test="${currentPage != null && totalPages != null}">
            <!-- 이전 페이지 -->
            <c:if test="${currentPage > 1}">
                <a href="?page=${currentPage - 1}" id="pagingbotton">이전</a>
            </c:if>

            <!-- 페이지 번호 -->
            <c:forEach var="i" begin="${startPage}" end="${endPage}">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <span>${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <!-- 다음 페이지 -->
            <c:if test="${currentPage < totalPages}">
                <a href="?page=${currentPage + 1}" id="pagingbotton">다음</a>
            </c:if>
            
            <br>
            
            <!-- 페이지 범위 출력 -->
            <p>현재 페이지 범위: ${startPage} - ${endPage} (총 페이지: ${totalPages})</p>
        </c:if>
    </div>

	<br>
	<div id="proContainer">인기상품?</div>
	<br>
	<div id="articleContainer">
		<table id="articletable">
			<thead id="article">
				<tr>
					<th></th>
					<th>번호</th>
					<th>제목</th>
					<th>조회수</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="article" items="${articles}">
					<tr>
						<td>${article.id}</td>
						<td>${article.boardid}</td>
						<td>
							<form action="/article/Detail" method="post">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}"> <input type="hidden" name="id"
									value="${article.id}">
								<button type="submit">${article.name}</button>
							</form>
						</td>
						<td>${article.viewcount}</td>
						<td>${article.regDate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<%@ include file="includes/foot1.jsp"%>

</body>
</html>