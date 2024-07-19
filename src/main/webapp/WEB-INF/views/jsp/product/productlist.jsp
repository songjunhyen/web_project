<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.demo.vo.Product"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리스트</title>
</head>
<%@ include file="../includes/head1.jsp"%>
<body>
	<div class="table-box-type">
		<table>
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
				<c:forEach var="products" items="${products}">
					<tr>
						<td>${products.id}</td>
						<td>${products.category}</td>
						<td>
							<form action="/product/Detail" method="post">
								<input type="hidden" name="id" value="${products.id}">
								<button type="submit">${products.name}</button>
							</form>
						</td>
						<td>${products.price}</td>
						<td>${products.viewcount}</td>
						<td>${products.regDate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>	
			<%-- 페이징 버튼 --%>
	<div>
	    <%-- 이전 페이지 --%>
	    <c:if test="${currentPage > 1}">
	        <a href="?page=${currentPage - 1}">이전</a>
	    </c:if>
	
	    <%-- 페이지 번호 --%>
	    <c:forEach begin="1" end="${totalPages}" varStatus="loop">
	        <c:choose>
	            <c:when test="${loop.index == currentPage}">
	                <strong>${loop.index}</strong>
	            </c:when>
	            <c:otherwise>
	                <a href="?page=${loop.index}">${loop.index}</a>
	            </c:otherwise>
	        </c:choose>
	    </c:forEach>
	
	    <%-- 다음 페이지 --%>
	    <c:if test="${currentPage < totalPages}">
	        <a href="?page=${currentPage + 1}">다음</a>
	    </c:if>
	</div>
			
		<button class="btn btn-active btn-sm" onclick="location.href='/Home/Main'">메인으로</button>
	</div>
	
</body>
</html>