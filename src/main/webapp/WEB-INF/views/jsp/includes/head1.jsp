<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.demo.vo.Product"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
	align-items: center; /* 탑바의 아이템들을 수직으로 가운데 정렬 */
	background-color: #333;
	padding: 10px;
	height: 25px; /* 탑바의 높이 설정 */
}

.top-bar ul {
	display: flex;
	align-items: center; /* 탑바 내의 아이템을 수직으로 가운데 정렬 */
}

.top-bar li {
	margin-left: 20px;
}

.top-bar a {
	color: white;
	text-decoration: none;
	padding: 10px;
	display: flex;
	align-items: center; /* 수직 가운데 정렬 */
	height: 100%; /* 탑바의 전체 높이 차지 */
}

.top-bar a:hover {
	background-color: #ddd;
	color: black;
	border-radius: 5px;
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

.logout-button {
	color: white;
	text-decoration: none;
	padding: 10px; /* 좌우 패딩을 설정하여 버튼 크기 조정 */
	height: 100%; /* 버튼 높이를 부모 요소(tap-bar)의 높이와 맞춤 */
	background: none;
	border: none;
	cursor: pointer;
	font: inherit;
	display: flex;
	align-items: center; /* 수직 가운데 정렬 */
	justify-content: center; /* 수평 가운데 정렬 */
	box-sizing: border-box; /* 패딩과 테두리를 포함한 전체 크기 조정 */
}

.logout-button:hover {
	background-color: #ddd;
	color: black;
	border-radius: 5px;
}
</style>

<header>
	<nav class="top-bar">
		<ul>
			<li><a href="/Home/Main">Home</a></li>
			<sec:authorize access="isAuthenticated()">
				<c:choose>
					 <c:when test="${userRole == 'user'}">
						<li>
							<form id="logoutForm" action="/Home/logout" method="post"
								style="display: inline;">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" /> <input type="submit" value="Logout"
									class="logout-button" />
							</form>
						</li>
						<li><a href="/user/Modify">Modify</a></li>
						<li><a href="/Cart/List">Cart</a></li>
					</c:when>
					<c:when test="${userRole == 'admin'}">
						<li>
							<form id="logoutForm" action="/Home/logout" method="post"
								style="display: inline;">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" /> <input type="submit" value="Logout"
									class="logout-button" />
							</form>
						</li>
						<li><a href="/product/add">Registration</a></li>
						<c:if test="${adminClass == 1}">
							<li><a href="/admin/Dashboard">Dashboard</a></li>
							<li><a href="/admin/Modify">Modify</a></li>
						</c:if>
					</c:when>
				</c:choose>
			</sec:authorize>
			<sec:authorize access="!isAuthenticated()">
				<li><a href="/Home/login">Login</a></li>
				<li><a href="/user/Signup">Join</a></li>
				<li><a href="/temp/Cart">Cart</a></li>
			</sec:authorize>
			<li><a href="/product/list">Product</a></li>
			<li><a href="#">Help</a></li>
		</ul>
	</nav>
	<div class='title'>
		<p style="text-align: center;">
			<a href="/Home/Main" target="_blank">E-커머스 프로젝트</a>
		</p>
	</div>
</header>

