<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장바구니 목록</title>
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
    </div>
	<%@ include file="../includes/foot1.jsp"%>
</body>
</html>