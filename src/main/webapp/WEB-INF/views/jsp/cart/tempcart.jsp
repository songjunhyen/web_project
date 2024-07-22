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
<script>
function modifyCartItem(productid, color, size, count, price) {
    let cookies = document.cookie.split(';');
    let cartData = '';
    let found = false;
    let cartCookieName = 'cart=';

    // 쿠키에서 장바구니 데이터 읽기
    for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i].trim();
        if (cookie.indexOf(cartCookieName) === 0) {
            cartData = cookie.substring(cartCookieName.length);
            found = true;
            break;
        }
    }

    // 장바구니 데이터 처리
    let newCartData = '';
    if (found) {
        let items = cartData.split('|');
        let updated = false;

        for (let i = 0; i < items.length; i++) {
            let item = items[i].split(':');
            if (item.length === 6 && item[0] === productid && item[2] === color && item[3] === size) {
                newCartData += `${productid}:${item[1]}:${color}:${size}:${count}:${price}|`;
                updated = true;
            } else {
                newCartData += items[i] + '|';
            }
        }

        if (!updated) {
            newCartData += `${productid}:${item[1]}:${color}:${size}:${count}:${price}|`;
        }
    } else {
        newCartData = `${productid}:${color}:${size}:${count}:${price}|`; // Ensure `name` is correctly defined
    }

    // 쿠키 설정: 장바구니 데이터 저장
    document.cookie = `cart=${newCartData.slice(0, -1)};path=/;max-age=86400`; // Remove trailing '|'
    
    // 페이지 새로 고침
    window.location.reload();
}

function deleteCartItem(productid, color, size) {
    let cookies = document.cookie.split(';');
    let cartData = '';
    let found = false;
    let cartCookieName = 'cart=';

    // 쿠키에서 장바구니 데이터 읽기
    for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i].trim();
        if (cookie.indexOf(cartCookieName) === 0) {
            cartData = cookie.substring(cartCookieName.length);
            found = true;
            break;
        }
    }

    // 장바구니 데이터 처리
    let newCartData = '';
    if (found) {
        let items = cartData.split('|');

        for (let i = 0; i < items.length; i++) {
            let item = items[i].split(':');
            if (item.length === 6 && (item[0] !== productid || item[2] !== color || item[3] !== size)) {
                newCartData += items[i] + '|';
            }
        }
    }

    // 쿠키 설정: 장바구니 데이터 저장
    if (newCartData.endsWith('|')) {
        newCartData = newCartData.slice(0, -1); // Remove trailing '|'
    }
    document.cookie = `cart=${newCartData};path=/;max-age=86400`;

    // 페이지 새로 고침
    window.location.reload();
}
</script>
</head>
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
                        <th>수정</th>
                        <th>삭제</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cart" items="${carts}">
                        <tr>
                            <td>${cart.productid}</td>
                            <td>${cart.name}</td>
                            <td>
                                <form onsubmit="modifyCartItem('${cart.productid}', '${cart.color}', '${cart.size}', this.count.value, ${cart.price}); return false;">
                                    <input type="number" name="count" step="1" min="1" max="100" value="${cart.count}">
                                    <button type="submit">수정</button>
                                </form>
                            </td>
                            <td>${cart.price * cart.count}</td>
                            <td>${cart.color}</td>
                            <td>${cart.size}</td>
                            <td>
                                <button onclick="modifyCartItem('${cart.productid}', '${cart.color}', '${cart.size}', this.previousElementSibling.value, ${cart.price});">수정</button>
                            </td>
                            <td>
                                <button onclick="deleteCartItem('${cart.productid}', '${cart.color}', '${cart.size}');">삭제</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>