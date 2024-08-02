<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<style>
.error-message {
    color: red;
    display: none;
}
</style>

    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">

<script>
var csrfToken;
var csrfHeader;

    $(document).ready(function() {
        csrfToken = $('meta[name="_csrf"]').attr('content');
        csrfHeader = $('meta[name="_csrf_header"]').attr('content');

        function validateForm() {
            var isUseridValid = checkUserid();
            var isPasswordValid = checkPassword();
            var isPasswordMatchValid = checkPasswordMatch();
            var isNameValid = checkEmptyInput("name", "nameError", "닉네임을 입력해주세요.");
            var isEmailValid = checkEmail();
            var isAddressValid = checkAddress();

            return isUseridValid && isPasswordValid && isPasswordMatchValid && isNameValid && isEmailValid && isAddressValid;
        }

        $("form").submit(function(event) {
            if (!validateForm()) {
                event.preventDefault();
            } else {
                updateFullAddress(); // 폼 제출 전에 전체 주소 업데이트
            }
        });

        $("#userid").blur(function() {
            checkUserid();
        });

        $("#pw").blur(function() {
            checkPassword();
        });

        $("#userpw2").blur(function() {
            checkPasswordMatch();
        });

        $("#name").blur(function() {
            checkEmptyInput("name", "nameError", "닉네임을 입력해주세요.");
        });

        $("#email").blur(function() {
            checkEmail();
        });

        $("#frontaddress").blur(function() {
            checkAddress();
        });

        function showError(errorId, errorMessage) {
            $("#" + errorId).html(errorMessage);
            $("#" + errorId).show();
        }

        function hideError(errorId) {
            $("#" + errorId).html("");
            $("#" + errorId).hide();
        }

        function checkEmptyInput(fieldId, errorMessageId, errorMessage) {
            var fieldValue = $("#" + fieldId).val().trim();
            if (fieldValue === "") {
                showError(errorMessageId, errorMessage);
                return false;
            } else {
                hideError(errorMessageId);
                return true;
            }
        }

        function checkPasswordMatch() {
            var pw = $("#pw").val().trim();
            var userpw2 = $("#userpw2").val().trim();

            if (pw !== userpw2) {
                showError("pw2Error", "비밀번호가 일치하지 않습니다.");
                return false;
            } else {
                hideError("pw2Error");
                return true;
            }
        }

        function checkPassword() {
            var pw = $("#pw").val();
            if (pw === "") {
                showError("pwError", "비밀번호를 입력해주세요.");
                return false;
            }

            if (pw.includes(" ")) {
                showError("pwError", "공백을 사용할 수 없습니다.");
                return false;
            }

            hideError("pwError");
            return true;
        }

        function checkUserid() {
        	let status = $('#userid').attr('status');
            var id = $("#userid").val();
            var specialCharPattern = /[^a-zA-Z0-9]/;

            if (id === "") {
                showError("useridError", "아이디를 입력해주세요.");
                return false;
            }

            if (specialCharPattern.test(id)) {
                showError("useridError", "아이디에는 특수문자나 공백이 포함될 수 없습니다.");
                return false;
            }

            if (id.includes(" ")) {
                showError("useridError", "아이디에 공백을 사용할 수 없습니다.");
                return false;
            }      

            hideError("useridError");
            return true;
        }    	

        function checkEmail() {
            var email = $("#email").val().trim();
            var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

            if (email === "") {
                showError("emailError", "입력해주세요.");
                return false;
            }

            if (!emailPattern.test(email)) {
                showError("emailError", "유효한 이메일 주소가 아닙니다.");
                return false;
            }
	
            hideError("emailError");
            return true;
        }

        function checkAddress() {
            var frontAddress = $("#frontaddress").val().trim();
            var detailAddress = $("#detailAddress").val().trim();

            if (frontAddress === "" || detailAddress === "") {
                showError("addressError", "주소와 상세주소를 입력해주세요.");
                return false;
            }

            hideError("addressError");
            return true;
        }

    });
</script>
</head>
<%@ include file="../includes/head1.jsp"%>
<body>
    <form:form modelAttribute="SignUpForm" action="/user/signup" method="post">
        <form:hidden path="${_csrf.parameterName}" value="${_csrf.token}" />

        <label for="userid">아이디:</label>
        <br>
        <input type="text" id="userid" name="userid" placeholder="아이디를 입력해주세요">
        <br>
        <button id="checkUseridButton" type="button">중복확인</button>
        <br>
        <div id="useridError" class="error-message"></div>
        <br>

        <label for="pw">비밀번호:</label>
        <br>
        <input type="password" id="pw" name="pw" placeholder="비밀번호를 입력해주세요">
        <br>
        <div id="pwError" class="error-message"></div>
        <br>

        <label for="userpw2">비밀번호 확인:</label>
        <br>
        <input type="password" id="userpw2" name="userpw2" placeholder="비밀번호를 입력해주세요">
        <br>
        <div id="pw2Error" class="error-message"></div>
        <br>

        <label for="name">닉네임:</label>
        <br>
        <input type="text" id="name" name="name" placeholder="닉네임을 입력해주세요">
        <br>
        <div id="nameError" class="error-message"></div>
        <br>

        <label for="email">이메일:</label>
        <br>
        <input type="text" id="email" name="email" placeholder="이메일을 입력해주세요">
        <br>
        <button id="checkUseridButton2" type="button">중복확인</button>
        <br>
        <div id="emailError" class="error-message"></div>
        <br>

        <label for="postcode">우편번호:</label>
        <br>
        <input type="text" id="postcode" name="postcode" placeholder="우편번호" readonly>
        <input type="button" onclick="openPostcodePopup()" value="우편번호 찾기">
        <br>

        <label for="frontaddress">도로명 주소:</label>
        <input type="text" id="frontaddress" name="frontaddress" placeholder="도로명 주소" readonly>
        <br>

        <label for="detailAddress">상세주소:</label>
        <input type="text" id="detailAddress" name="detailAddress" placeholder="상세주소">
        <br>

        <!-- 통합 주소를 설정할 필드 -->
        <input type="hidden" id="address" name="address">

        <input type="submit" value="회원가입">
    </form:form>
    
    <script>

    function openPostcodePopup() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = '';
                var extraAddr = '';

                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else {
                    addr = data.jibunAddress;
                }

                if (data.userSelectedType === 'R') {
                    if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                    }
                    if (data.buildingName !== '' && data.apartment === 'Y') {
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    if (extraAddr !== '') {
                        extraAddr = ' (' + extraAddr + ')';
                    }
                } else {
                    extraAddr = '';
                }

                $("#postcode").val(data.zonecode);
                $("#frontaddress").val(addr);
                $("#extraAddress").val(extraAddr); // 사용하지 않음
                $("#detailAddress").focus();
            }
        }).open();
    }

    function updateFullAddress() {
        var postcode = $("#postcode").val().trim(); // 우편번호
        var frontAddress = $("#frontaddress").val().trim(); // 도로명 주소
        var detailAddress = $("#detailAddress").val().trim(); // 상세 주소

        var fullAddress = postcode + ' ' + frontAddress + (detailAddress ? ' ' + detailAddress : ''); // 전체 주소 생성

        $("#address").val(fullAddress); // 통합된 주소를 `address` 필드에 설정
    }
    
    function checkUseridap() {            
        var userid = $("#userid").val().trim();

        $.ajax({
            url: 'checkId.do',
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
               var $message = $('#userid').next('.checkIdSpan');
            
	            if (data.cnt > 0) {
	                $('#userid').attr('status', 'no');
	                if ($message.length) {
	                    // 메시지가 이미 존재하면 업데이트
	                    $message.text('이미 존재하는 아이디입니다.').css('color', 'red');
	                } else {
	                    // 메시지가 존재하지 않으면 새로 추가
	                    $('#userid').after("<span class='checkIdSpan' style='color:red'>이미 존재하는 아이디입니다.</span>");
	                }
	            } else {
	                $('#userid').attr('status', 'yes');
	                if ($message.length) {
	                    // 메시지가 이미 존재하면 업데이트
	                    $message.text('사용 가능한 아이디입니다.').css('color', 'blue');
	                } else {
	                    // 메시지가 존재하지 않으면 새로 추가
	                    $('#userid').after("<span class='checkIdSpan' style='color:blue'>사용 가능한 아이디입니다.</span>");
	                }
	            }
            },
            error: function(e) {
                alert("아이디 중복 확인 중 오류가 발생했습니다.");
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
    
	$("#checkUseridButton").click(function() {
        checkUseridap();
    });
	
	$("#checkUseridButton2").click(function() {
        checkEmailap();
    });
    
</script>
    <%@ include file="../includes/foot1.jsp"%>
</body>
</html>