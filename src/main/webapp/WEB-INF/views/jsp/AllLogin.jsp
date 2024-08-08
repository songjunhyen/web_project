<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">

    <meta name="google-signin-client_id" content="228463015999-drdvgd3jcm635f8u8j8hp7g0cp3ha988.apps.googleusercontent.com">
    <script src="https://accounts.google.com/gsi/client" async defer></script>

    <script>
    var csrfToken;
    var csrfHeader;

    $(document).ready(function() {
        csrfToken = $('meta[name="_csrf"]').attr('content');
        csrfHeader = $('meta[name="_csrf_header"]').attr('content');    

        console.log('CSRF Token:', csrfToken);
        console.log('CSRF Header:', csrfHeader);

        $.get('/api/auth/check', function(response) {
            console.log('Authentication status:', response);
            if (response === 'User is not authenticated') {
                console.log('You have been logged out successfully.');
            } else {
                console.log('Log out failed. Please try again.');
            }
        }).fail(function() {
            console.log('Error checking authentication status.');
        });
        
        $("#userid").blur(function() {
            checkEmptyInput("userid", "useridError", "아이디를 입력해주세요.");
        });

        $("#pw").blur(function() {
            checkEmptyInput("pw", "pwError", "비밀번호를 입력해주세요.");
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
            var errorMessageElement = $("#" + errorMessageId);

            if (fieldValue === "") {
                showError(errorMessageId, errorMessage);
                return false;
            } else {
                hideError(errorMessageId);
                return true;
            }
        }
    });

    </script>
</head>
<%@ include file="includes/head1.jsp"%>
<body>
	<h1>Login</h1>
	<form action="/Home/Login" method="post">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" /> <label for="userid">아이디:</label> <br>
		<input type="text" id="userid" name="userid" placeholder="아이디를 입력해주세요" />
		<br>
		<div id="useridError" class="error-message"></div>
		<br> <label for="pw">비밀번호:</label> <br> <input
			type="password" id="pw" name="pw" placeholder="비밀번호를 입력해주세요" /> <br>
		<div id="pwError" class="error-message"></div>
		<br>
		<button type="submit">Login</button>
	</form>
	
<body>
	<h1>Login</h1>
    <a href="/oauth2/authorization/google">Login with Google</a>
</body>

	<%@ include file="includes/foot1.jsp"%>
</body>

</html>