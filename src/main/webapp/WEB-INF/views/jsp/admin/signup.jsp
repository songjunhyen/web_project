<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 추가</title>
</head>
<%@ include file="../includes/head1.jsp"%>
<body>

	생성 버튼 눌러서 만들어지면 비동기적으로 만들어진 id, pw 접속에 관련된 정보 출력

	<p>부서(class)선택</p>
	<select name="adminclass">
		<option value="2">2</option>
		<option value="3">3</option>
		<option value="4">4</option>
		<option value="5">5</option>
		<option value="6">6</option>
	</select>

	<p>정보 입력</p>
	<label for="name">이름:</label>
	<br>
	<input type="text" id="name" name="name" placeholder="이름을 입력해주세요" />
	<br>
	<br>
	<label for="email">이메일 :</label>
	<br>
	<input type="text" id="email" name="email"
		placeholder="사용할 E-mail 주소를 입력해주세요" />

	<p>사용할 버튼</p>
	<button type="submit">Add</button>
	
	
	<div id = "addresult">
		정상적으로 생성되었을 경우 생성된 admin정보 출력 공간
	</div>

</body>
<%@ include file="../includes/foot1.jsp"%>
</html>