<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
$(document).ready(function() {
    // JSP에서 전달한 result 값을 JavaScript 변수로 설정
    var result = '${result}';
    console.log("Resulted value: ", result);  // 콘솔에서 확인

    // 결과에 따라 계정 관리 섹션의 표시 여부 결정
    if (result === 'true') {
        $("#accountManagement").show();
    } else {
        $("#accountManagement").hide();
    }

    // 폼 제출 시 유효성 검사
    $("form").submit(function(event) {
        if (!validateForm()) {
            event.preventDefault();
        } else {
            updateFullAddress(); // 폼 제출 전에 전체 주소 업데이트
        }
    });

    // 개별 필드에 대한 유효성 검사
    $("#pw").blur(function() {
        checkPassword();
    });

    $("#name").blur(function() {
        checkEmptyInput("name", "nameError", "이름을 입력해주세요.");
    });

    $("#email").blur(function() {
        checkEmail();
    });

    $("#frontaddress").blur(function() {
        checkAddress();
    });

    // 우편번호 찾기 팝업
    $("#postcodeButton").click(function() {
        openPostcodePopup();
    });
});

// 비밀번호 유효성 검사
function checkPassword() {
    var pw = $("#pw").val();
    if (pw === "") {
        showError("pwError", "비밀번호를 입력해주세요.");
        return false;
    }

    if (pw.includes(" ")) {
        showError("pwError", "비밀번호에 공백을 사용할 수 없습니다.");
        return false;
    }

    hideError("pwError");
    return true;
}

// 이름 유효성 검사
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

// 이메일 유효성 검사
function checkEmail() {
    var email = $("#email").val().trim();
    var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (email === "") {
        showError("emailError", "이메일을 입력해주세요.");
        return false;
    }

    if (!emailPattern.test(email)) {
        showError("emailError", "유효한 이메일 주소가 아닙니다.");
        return false;
    }

    hideError("emailError");
    return true;
}

// 주소 유효성 검사
function checkAddress() {
    var frontAddress = $("#frontaddress").val().trim();
    var detailAddress = $("#detailAddress").val().trim();

    if (frontAddress === "" || detailAddress === "") {
        showError("addressError", "도로명 주소와 상세주소를 입력해주세요.");
        return false;
    }

    hideError("addressError");
    return true;
}

// 에러 메시지 표시
function showError(errorId, errorMessage) {
    $("#" + errorId).html(errorMessage);
    $("#" + errorId).show();
}

// 에러 메시지 숨기기
function hideError(errorId) {
    $("#" + errorId).html("");
    $("#" + errorId).hide();
}

// 주소 찾기 팝업 열기
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
            $("#detailAddress").focus();
        }
    }).open();
}

// 전체 주소를 통합하여 `address` 필드에 설정
function updateFullAddress() {
    var postcode = $("#postcode").val().trim(); // 우편번호
    var frontAddress = $("#frontaddress").val().trim(); // 도로명 주소
    var detailAddress = $("#detailAddress").val().trim(); // 상세 주소

    var fullAddress = postcode + ' ' + frontAddress + (detailAddress ? ' ' + detailAddress : ''); // 전체 주소 생성

    $("#address").val(fullAddress); // 통합된 주소를 `address` 필드에 설정
}
</script>
</head>
<%@ include file="../includes/head1.jsp"%>
<body>
<div>
계정 삭제의 경우 복구가 불가능합니다.
만약 계정 관리창이 보이지 않는 경우 제대로 된 경로로 접속하지 않은 것입니다.
</div>
<div id="accountManagement" style="display: none;">
    <h2>계정 관리</h2>
    <form action="/user/modify" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        
        <label for="pw">비밀번호:</label>
        <br>
        <input type="password" id="pw" name="pw" placeholder="비밀번호를 입력해주세요" required />
        <div id="pwError" class="error-message"></div>
        <br>

        <label for="name">이름:</label>
        <br>
        <input type="text" id="name" name="name" placeholder="이름을 입력해주세요" required />
        <div id="nameError" class="error-message"></div>
        <br>

        <label for="email">이메일:</label>
        <br>
        <input type="email" id="email" name="email" placeholder="이메일을 입력해주세요" required />
        <div id="emailError" class="error-message"></div>
        <br>

        <label for="postcode">우편번호:</label>
        <br>
        <input type="text" id="postcode" name="postcode" placeholder="우편번호" readonly>
        <input type="button" id="postcodeButton" value="우편번호 찾기">
        <br>

        <label for="frontaddress">도로명 주소:</label>
        <input type="text" id="frontaddress" name="frontaddress" placeholder="도로명 주소" readonly>
        <br>

        <label for="detailAddress">상세주소:</label>
        <input type="text" id="detailAddress" name="detailAddress" placeholder="상세주소">
        <br>

        <!-- 통합 주소를 설정할 필드 -->
        <input type="hidden" id="address" name="address">

        <button type="submit">수정</button>
    </form>
    <br>
    <br>
    <a href="/user/Signout" onclick="confirmDelete(event)">
        <button type="button">삭제</button>
    </a>
</div>
</body>
<%@ include file="../includes/foot1.jsp"%>
</html>