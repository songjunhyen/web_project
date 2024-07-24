<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.demo.vo.Product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>



<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
	$(document).ready(function() {
		var csrfHeader = $('meta[name="csrf-header"]').attr('content');
		var csrfToken = $('meta[name="csrf-token"]').attr('content');

		$.ajaxSetup({
			beforeSend : function(xhr) {
				xhr.setRequestHeader(csrfHeader, csrfToken);
			}
		});
	});
</script>
<style>
body, ul, li {
	list-style: none;
	padding: 0;
	margin: 0;
}

.top-bar {
	display: flex;
	justify-content: flex-end; 
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
				<li><a href="/temp/Cart">Cart</a></li>
			</c:if>
			<c:if test="${sessionScope['class'] == 'user'}">
				<li><a href="/user/Logout">Logout</a></li>
				<li><a href="/user/Modify">Modify</a></li>
				<li><a href="/Cart/List">Cart</a></li>
			</c:if>
			<c:if test="${sessionScope['class'] == 'admin'}">
				<li><a href="/admin/Logout">Logout</a></li>
				<li><a href="/product/add">Registration</a></li>
				<c:if test="${sessionScope.adminclass == '0'}">
					<li><a href="/admin/Modify">Modify</a></li>
				</c:if>
			</c:if>
			<c:if test="${empty sessionScope.islogined}">
				<li><a href="/admin/Login">Admin</a></li>
			</c:if>
			<li><a href="/Home/Main">Product</a></li>
			<li><a href="#">Help</a></li>
		</ul>
	</nav>
	<div class='title'>
		<p style="text-align: center;">
			<a href="Main" target="_blank">E-커머스 프로젝트</a>
		</p>
	</div>
</header>

