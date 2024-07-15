<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
 <style>
        .error-message {
            color: red;
            display: none;
        }
    </style>
    <script>
        $(document).ready(function() {
            // 입력 필드에서 포커스를 잃었을 때 유효성 검사
            $("#userid").blur(function() {
                checkEmptyInput("userid", "useridError", "아이디를 입력해주세요.");
            });

            $("#userpw").blur(function() {
                checkEmptyInput("userpw", "userpwError", "비밀번호를 입력해주세요.");
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

            // 아이디, 비밀번호가 비어 있는지 확인하는 함수
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
        });
    </script>
</head>
<body>
<a href="/">Home</a>
<br>
<form id="loginForm" action="/user/login" method="post">
    <label for="userid">아이디:</label><br>
    <input type="text" id="userid" name="userid" placeholder="아이디를 입력해주세요"><br>
    <div id="useridError" class="error-message"></div><br>

    <label for="pw">비밀번호:</label><br>
    <input type="password" id="pw" name="pw" placeholder="비밀번호를 입력해주세요"><br>
    <div id="userpwError" class="error-message"></div><br>

    <input type="submit" value="로그인">
</form>
</body>
</html>