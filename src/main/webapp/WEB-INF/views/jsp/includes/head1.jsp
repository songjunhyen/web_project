<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.demo.vo.Product"%>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<style>
	body, ul, li{
	  list-style:none;
	  padding: 0;
	  margin: 0;	
	}
	 .top-bar {
        display: flex;
        justify-content: flex-end; /* 우측 정렬 */
        background-color: #333;
        padding: 10px;
    }
    .top-bar ul {
        display: flex;
    }
    .top-bar li {
        margin-left: 20px;
    }
     .top-bar a {
        color: white;
        text-decoration: none;
        padding: 10px;
    }
    .top-bar a:hover {
        background-color: #ddd;
        color: black;
    }
    .title {
        text-align: center;
        margin-top: 20px;
    }
    .title a {
        font-size: 24px;
        color: #333;
        text-decoration: none;
    }	
</style>

<header>
	<nav class="top-bar">
		<ul>
			<li><a href="/Home/Main">Home</a></li>
			<c:if test="${empty sessionScope.islogined}">
				<li><a href="/user/Login">Login</a></li>
				<li><a href="/user/Signup">Join</a></li>
			</c:if>
			<c:if test="${sessionScope.islogined eq 1}">
				<c:choose>
					<c:when test="${empty session.getAttribute('adminclass')}">
						<li><a href="/user/Logout">Logout</a></li>
						<li><a href="/user/Modify">Modify</a></li>
					</c:when>
					<c:when test="${sessionScope.adminclass eq 0}">
						<li><a href="/admin/Logout">Logout</a></li>
						<li><a href="/admin/Modify">Modify</a></li>
					</c:when>
					<c:otherwise>
						<!--  관리자며 class가 0이 아닌경우  -->
						<li><a href="/admin/Logout">Logout</a></li>
					</c:otherwise>
				</c:choose>
			</c:if>
			<li><a href="/Home/Main">Product</a></li>
			<li><a href="">Help</a></li>
		</ul>
	</nav>
	<div class='title'>
		<p style="text-align: center;">
			<a href="Main" target="_blank">사이트 이름</a>
		</p>
	</div>
	
</header>
<!--
로그인 안되었으면 로그인 회원가입 버튼 보이게
로그인 되었다면 로그아웃 회원정보 수정 버튼, 장바구니 보이게
class가 몇이상이면 상품 등록 버튼, 상품 수정 버튼 보이게 회원정보 수정 등 몇몇은 관리자용으로 주소가 다르지만 똑같이 생긴 버튼 보이게
vip레벨이 몇이상이고 class가 몇 이상이면 관리자 관리, 회원관리 버튼 보이게

즉 조건에 따라 더 많은 메뉴가 보이는
 -->
