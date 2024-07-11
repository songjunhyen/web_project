<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.demo.vo.Product"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>리스트</title>
</head>
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
						<td><a href="product/Detail?id=${products.id}">${products.name}</a></td>
						<td>${products.price}</td>
						<td>${products.viewcount}</td>
						<td>${products.regDate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>