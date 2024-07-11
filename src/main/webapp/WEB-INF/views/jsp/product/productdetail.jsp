<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.demo.vo.Product"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>제품 상세보기</title>
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
					<th>제조사</th>
					<th>색상</th>
					<th>사이즈</th>
					<th>제품설명</th>
					<th>조회수</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>${product.id}</td>
					<td>${product.category}</td>
					<td>${product.name}</td>
					<td>${product.price}</td>
					<td>${product.maker}</td>
					<td>${product.color}</td>
					<td>${product.size}</td>
					<td>${product.description}</td>
					<td>${product.viewcount}</td>
					<td>${product.regDate}</td>
				</tr>
			</tbody>
		</table>
	</div>
<div class="mt-3 text-sm">
    <button class="btn btn-active btn-sm" onclick="history.back();">뒤로가기</button>
    <a class="btn btn-active btn-sm" href="Modify?id=${product.id}">수정</a>
    <a class="btn btn-active btn-sm" href="Delete?id=${product.id}"
        onclick="if(confirm('정말 삭제하시겠습니까?') == false) return false;">삭제</a>
</div>
</body>
</html>
