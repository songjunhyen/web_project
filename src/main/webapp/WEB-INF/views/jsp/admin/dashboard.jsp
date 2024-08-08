<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Dash Board</title>
<style>
.alert {
	border: 1px solid #ccc;
	border-radius: 4px;
	padding: 15px;
	margin-bottom: 20px;
}

.alert-info {
	background-color: #e7f1ff;
	border-color: #d1e5f0;
}

.alert-danger {
	background-color: #fbe8e8;
	border-color: #f2c6c6;
}

.alert-warning {
	background-color: #fff4e5;
	border-color: #fbe4b9;
}

table {
	width: 100%;
	border-collapse: collapse;
}

table, th, td {
	border: 1px solid #ddd;
}

th, td {
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f2f2f2;
}

.button {
	background-color: #007bff;
	color: white;
	border: none;
	padding: 10px 20px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	margin: 4px 2px;
	cursor: pointer;
	border-radius: 4px;
}
</style>
</head>
<%@ include file="../includes/head1.jsp"%>
<body>

	<div id="adminprofile">
		<h2>관리자 정보</h2>
		<table>
			<thead>
				<tr>
					<th>번호</th>
					<th>부서</th>
					<th>ID</th>
					<th>이메일</th>
					<th>가입일</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>${admin.id}</td>
					<td>${admin.adminclass}</td>
					<td>${admin.adminId}</td>
					<td>${admin.email}</td>
					<td>${admin.regDate}</td>
				</tr>
			</tbody>
		</table>
	</div>

	<br>

	<div id="adminlink">
		<h2>사용 가능한 공통 기능</h2>
		<p>
			관리자 검색: <a href="/admin/Search" class="button">SearchA</a> (4)고객지원에서도 사용할 예정
		</p>		
		<p>
			유저 검색: <a href="/user/Search" class="button">SearchU</a> (5)
		</p>
	</div>

	<br>

	<div id="adminpower">
		<h2>관리자 권한 및 작업</h2>
		<ul>
			<c:when test="${admin.adminclass == 5}">
		
			</c:when>
			<c:when test="${admin.adminclass == 4}">

			</c:when>
			<c:when test="${admin.adminclass == 3}">
		
			</c:when>
			<c:when test="${admin.adminclass == 2}">

			</c:when>
			<c:when test="${admin.adminclass == 1}">
				<li><a href="/admin/Signup" class="button">Add</a></li>
			<br>
				<li><a href="/admin/Modify" class="button">Modify</a></li>
			</c:when>
			<c:when test="${admin.adminclass != 1}">
				<li><a href="/admin/Modify1" class="button">Modify</a></li>
			</c:when>
			
			<li><a href="/admin/Signout" class="button">Delete</a> (3)</li>
		</ul>
		
	</div>

	<br>

	<div class="alert alert-info">
		<h2>환영합니다, ${admin.name}님!</h2>
		<h3>정책 및 규정 안내</h3>
		<p>관리자 권한을 사용하여 시스템을 관리할 때에는 다음의 정책을 준수해 주세요:</p>
		<ul>
			<li>모든 변경 사항은 적절한 승인 절차를 거쳐야 합니다.</li>
			<li>데이터 보안과 사용자 프라이버시를 우선시해야 합니다.</li>
			<li>정기적으로 시스템 로그를 검토하고 이상 징후를 모니터링해야 합니다.</li>
		</ul>
		<p>정책 위반 시에는 관리자 권한이 제한될 수 있습니다.</p>

		<h3>현재 시스템 상태</h3>
		<p>현재 시스템의 상태는 양호합니다. 최근 업데이트 사항은 다음과 같습니다:</p>
		<ul>
			<li>2024년 8월 1일: 새로운 사용자 가입 기능이 추가되었습니다.</li>
			<li>2024년 7월 28일: 보안 패치가 적용되었습니다.</li>
		</ul>

		<h3>시스템 업데이트 공지</h3>
		<p>시스템의 정기적인 유지보수 작업이 예정되어 있습니다:</p>
		<ul>
			<li>유지보수 기간: 2024년 8월 5일 02:00 ~ 06:00</li>
			<li>영향: 모든 사용자에게 일시적인 서비스 중단이 발생할 수 있습니다.</li>
			<li>작업 내용: 서버 성능 향상 및 보안 패치 적용</li>
		</ul>
		<p>업데이트 동안 시스템에 대한 접근이 제한될 수 있습니다. 양해 부탁드립니다.</p>
	</div>

</body>
<%@ include file="../includes/foot1.jsp"%>
</html>