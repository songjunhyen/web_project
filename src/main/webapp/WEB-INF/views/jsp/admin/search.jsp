<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 검색</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">

<script>

var csrfToken;
var csrfHeader;
$(document).ready(function() {
    csrfToken = $('meta[name="_csrf"]').attr('content');
    csrfHeader = $('meta[name="_csrf_header"]').attr('content');
	});
</script>
</head>
<%@ include file="../includes/head1.jsp"%>
<body>

검색창 검색할때마다 리스트 갱신하도록

키워드는 부서, 이름, 이메일

찾기 버튼 누르면 입력칸이 비었는지 체크해서 있으면 있는걸로 만약 다 비었으면 adminclass 2이상인 관리자 전원 나오게

리스트는

클래스, 이름, 이메일
클래스, 이름, 이메일
클래스, 이름, 이메일
클래스, 이름, 이메일
클래스, 이름, 이메일
클래스, 이름, 이메일
클래스, 이름, 이메일

이런식으로 리스트 나오게 

<div>
 <form action="/admin/Searching" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <label for="adminclass">클래스:</label>
        <input type="hidden" name="adminclass" value="관리부서">
        <br>
			<input type="hidden" name="adminclass" value="관리부서"> 
			  <select name="adminclass">
				 <option value=""> </option>
				 <option value="2">고객지원팀</option>
				 <option value="3">사용자 관리팀</option>
				 <option value="4">결제문의팀</option>
				 <option value="5">상품관리팀</option>
				 <option value="6">기타</option>
			  </select>
		<br>
        <label for="name">이름:</label>
        <br>
        <input type="text" id="name" name="name" placeholder="이름을 입력해주세요" />
        <br>
        <label for="email">이메일:</label>
        <br>
        <input type="text" id="email" name="email" placeholder="이메일을 입력해주세요" />
        <br>
        <button type="submit" id ="searchbutton">Search</button>
    </form>
</div>
<div>

검색 결과


</div>
 <script>
 	function checksearchbutton(){
 		var adminclass = $("#adminclass").val().trim();
 		var name =  $("#name").val().trim();
 		var email = $("#email").val().trim();
 		
 		if(adminclass=="" && name=""){
 			checkEmailap(email);
 		}else if(name=="" && email=""){
 			checkAdminclassap(adminclass);
 		}else if(adminclass=="" && email=""){
 			checkNameap(name);
 		}else if(adminclass==""){
 			check ap();
 		}else if(name=""){
 			check ap();
 		}else if(email=""){
 			check ap();
 		}
 	}

    function checkNameap(name) {     
        $.ajax({
            url: 'checkName.do',
            type: 'POST',
            data: {
                userid: userid
            },
            beforeSend: function(xhr) {
                // CSRF 토큰을 헤더에 추가
                if (csrfToken && csrfHeader) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            },
            success: function(data) {
                // 아이디가 존재한다면
               var $message = $('#name').next('.checkNameSpan');
            
	            if (data.cnt > 0) {
	                $('#name').attr('status', 'no');
	                if ($message.length) {
	                    // 메시지가 이미 존재하면 업데이트
	                    $message.text('해당되는 관리자 리스트입니다.').css('color', 'blue');
	                    
	                } else {
	                    // 메시지가 존재하지 않으면 새로 추가
	                    $('#name').after("<span class='checkIdSpan' style='color:blue'>해당되는 관리자 리스트입니다</span>");
	                }
	            } else {
	                $('#name').attr('status', 'yes');
	                if ($message.length) {
	                    // 메시지가 이미 존재하면 업데이트
	                    $message.text('존재하지 않는 관리자입니다.').css('color', 'red');
	                } else {
	                    // 메시지가 존재하지 않으면 새로 추가
	                    $('#name').after("<span class='checkIdSpan' style='color:red'>존재하지 않는 관리자입니다.</span>");
	                }
	            }
            },
            error: function(e) {
                alert("확인 중 오류가 발생했습니다.");
            }
        });
    }
    
    function checkEmailap() {            
        var email = $("#email").val().trim();

        $.ajax({
            url: 'checkEmail.do',
            type: 'POST',
            data: {
               email: email
            },
            beforeSend: function(xhr) {
                // CSRF 토큰을 헤더에 추가
                if (csrfToken && csrfHeader) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            },
            success: function(data) {
                // 아이디가 존재한다면
               var $message = $('#email').next('.checkEmailSpan');
            
	            if (data.cnt > 0) {
	                $('#email').attr('status', 'no');
	                if ($message.length) {
	                    // 메시지가 이미 존재하면 업데이트
	                    $message.text('이미 등록된 이메일입니다.').css('color', 'red');
	                } else {
	                    // 메시지가 존재하지 않으면 새로 추가
	                    $('#email').after("<span class='checkEmailSpan' style='color:red'>이미 존재하는 아이디입니다.</span>");
	                }
	            } else {
	                $('#email').attr('status', 'yes');
	                if ($message.length) {
	                    // 메시지가 이미 존재하면 업데이트
	                    $message.text('사용 가능한 이메일입니다.').css('color', 'blue');
	                } else {
	                    // 메시지가 존재하지 않으면 새로 추가
	                    $('#email').after("<span class='checkEmailSpan' style='color:blue'>사용 가능한 아이디입니다.</span>");
	                }
	            }
            },
            error: function(e) {
                alert("아이디 중복 확인 중 오류가 발생했습니다.");
            }
        });
    }
    
	$("#searchbutton").click(function() {
        checksearchbutton();
    });
    
</script>
</body>
	<%@ include file="../includes/foot1.jsp"%>
</html>