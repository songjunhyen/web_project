<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 확인</title>

</head>
<%@ include file="../includes/head1.jsp"%>
<body>

	<h1>회원 프로필</h1>
	<p>회원님의 프로필 정보: [ ]</p>

	<h2>본인 확인</h2>
	<p>비밀번호를 입력하여 본인 확인을 진행하세요:</p>
	<form action="/user/Checking" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="password" id="pw" name="pw" placeholder="비밀번호를 입력하세요" required>
        <button type="submit">확인</button>
    </form>
	<br>
	<br>
	<br>
</body>

<%@ include file="../includes/foot1.jsp"%>
</html>