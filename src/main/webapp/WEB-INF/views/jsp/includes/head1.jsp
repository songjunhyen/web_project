<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.demo.vo.Product"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

<head>
<c:if test="${session.getAttribute('islogined') == 0}">
    <!-- 이 부분은 session의 id 속성이 1인 경우에만 실행됩니다
     로그인, 관리자창으로, 회원가입버튼 보이게  -->
</c:if>

<c:if test="${session.getAttribute('islogined') == 1}">
    <c:if test="${empty session.getAttribute('adminclass')}">
          <!-- adminclass가 null인 경우
          일반 유저인 경우 
          로그아웃 회원정보 수정 장바구니 구매내역  -->
    </c:if>
</c:if>


<c:if test="${session.getAttribute('islogined') == 1}">
    <c:choose>
        <c:when test="${empty session.getAttribute('adminclass')}">
            <!-- adminclass가 null인 경우 
            일반 유저인 경우 
            로그아웃 회원정보 수정 장바구니 구매내역  -->
        </c:when>
        <c:when test="${session.getAttribute('adminclass') == 0}">
            <!-- adminclass가 0인 경우 
            class가 0인 관리자인 경우
            관리자 전용 기능 보이게  -->
        </c:when>
        <c:otherwise>
          <!--  관리자며 class가 0이 아닌경우  -->
        </c:otherwise>
    </c:choose>
</c:if>

<!--
로그인 안되었으면 로그인 회원가입 버튼 보이게
로그인 되었다면 로그아웃 회원정보 수정 버튼, 장바구니 보이게
class가 몇이상이면 상품 등록 버튼, 상품 수정 버튼 보이게 회원정보 수정 등 몇몇은 관리자용으로 주소가 다르지만 똑같이 생긴 버튼 보이게
vip레벨이 몇이상이고 class가 몇 이상이면 관리자 관리, 회원관리 버튼 보이게

즉 조건에 따라 더 많은 메뉴가 보이는
 -->
