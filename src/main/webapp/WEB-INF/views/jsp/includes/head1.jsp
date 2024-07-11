<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>

세션으로 저장된 id 유무로 로그인 여부 확인
세션으로 저장된 class가 뭔지에 따라 노출되는 메뉴 바뀌게
세션으로 저장된 viplevel하고 class에 따라 노출되는 메뉴 범위 넓게

로그인 안되었으면 로그인 회원가입 버튼 보이게
로그인 되었다면 로그아웃 회원정보 수정 버튼, 장바구니 보이게
class가 몇이상이면 상품 등록 버튼, 상품 수정 버튼 보이게 회원정보 수정 등 몇몇은 관리자용으로 주소가 다르지만 똑같이 생긴 버튼 보이게
vip레벨이 몇이상이고 class가 몇 이상이면 관리자 관리, 회원관리 버튼 보이게

즉 조건에 따라 더 많은 메뉴가 보이는
</body>
</html>