<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
    .error-message {
        color: red;
        display: none;
    }
</style>
<script>
    $(document).ready(function() {
        // 아이디 입력 필드에서 포커스를 잃었을 때 유효성 검사
        $("#userid").blur(function() {
            checkEmptyInput("userid", "useridError", "아이디를 입력해주세요.");
        });

        // 비밀번호 입력 필드에서 포커스를 잃었을 때 유효성 검사
        $("#pw").blur(function() {
            checkEmptyInput("pw", "pwError", "비밀번호를 입력해주세요.");
        });

        // 비밀번호 확인 입력 필드에서 포커스를 잃었을 때 비밀번호 일치 여부 확인
        $("#userpw2").blur(function() {
            checkPasswordMatch();
        });

        // 닉네임 입력 필드에서 포커스를 잃었을 때 유효성 검사
        $("#name").blur(function() {
            checkEmptyInput("name", "nameError", "닉네임을 입력해주세요.");
        });

        // 이메일 입력 필드에서 포커스를 잃었을 때 유효성 검사
        $("#email").blur(function() {
            checkEmptyInput("email", "emailError", "이메일을 입력해주세요.");
        });

        // 주소 입력 필드에서 포커스를 잃었을 때 유효성 검사
        $("#address").blur(function() {
            checkEmptyInput("address", "addressError", "주소를 입력해주세요.");
        });

        // 입력 필드 오류 메시지 표시 함수
        function showError(errorId, errorMessage) {
            $("#" + errorId).html(errorMessage);
            $("#" + errorId).show();
        }

        // 입력 필드 오류 메시지 숨기는 함수
        function hideError(errorId) {
            $("#" + errorId).html("");
            $("#" + errorId).hide();
        }

        // 필드가 비어 있는지 확인하는 함수
        function checkEmptyInput(fieldId, errorMessageId, errorMessage) {
            var fieldValue = $("#" + fieldId).val().trim();
            var errorMessageElement = $("#" + errorMessageId);

            if (fieldValue === "") {
                errorMessageElement.html(errorMessage);
                errorMessageElement.show();
                return false; // 입력값이 비어있음
            } else {
                errorMessageElement.html("");
                errorMessageElement.hide();
                return true; // 입력값이 있음
            }
        }

        // 비밀번호와 비밀번호 확인이 일치하는지 확인하는 함수
        function checkPasswordMatch() {
            var pw = $("#pw").val().trim();
            var userpw2 = $("#userpw2").val().trim();
            var pw2Error = $("#pw2Error");

            if (pw !== userpw2) {
                pw2Error.html("비밀번호가 일치하지 않습니다.");
                pw2Error.show();
                return false;
            } else {
                pw2Error.html("");
                pw2Error.hide();
                return true;
            }
        }
    });
</script>
</head>
<body>
<form id="signupForm" action="/user/signup" method="post">
    <label for="userid">아이디:</label><br>
    <input type="text" id="userid" name="userid" placeholder="아이디를 입력해주세요"><br>
    <div id="useridError" class="error-message"></div><br>

    <label for="pw">비밀번호:</label><br>
    <input type="password" id="pw" name="pw" placeholder="비밀번호를 입력해주세요"><br>
    <div id="pwError" class="error-message"></div><br>

    <label for="userpw2">비밀번호 확인:</label><br>
    <input type="password" id="userpw2" name="userpw2" placeholder="비밀번호를 입력해주세요"><br>
    <div id="pw2Error" class="error-message"></div><br>

    <label for="name">닉네임:</label><br>
    <input type="text" id="name" name="name" placeholder="닉네임을 입력해주세요"><br>
    <div id="nameError" class="error-message"></div><br>

    <label for="email">이메일:</label><br>
    <input type="text" id="email" name="email" placeholder="이메일을 입력해주세요"><br>
    <div id="emailError" class="error-message"></div><br>

    <label for="address">주소:</label><br>
    <input type="text" id="address" name="address" placeholder="주소를 입력해주세요"><br>
    <div id="addressError" class="error-message"></div><br>

    <input type="submit" value="회원가입">
</form>
</body>
</html>