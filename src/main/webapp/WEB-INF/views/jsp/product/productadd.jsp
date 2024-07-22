<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 등록</title>
<style>
    .error-message {
        color: red;
        display: none;
    }
</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
$(document).ready(function() {
    function validateFile(inputId, errorId) {
        var fileInput = $("#" + inputId)[0];
        var file = fileInput.files[0];
        var maxFileSize = 5 * 1024 * 1024; // 5MB
        var allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;

        if (file) {
            if (file.size > maxFileSize) {
                showError(errorId, "파일 크기는 5MB를 초과할 수 없습니다.");
                $("#addForm").find("input[type='submit']").prop("disabled", true); // 제출 버튼 비활성화
            } else if (!allowedExtensions.exec(file.name)) {
                showError(errorId, "이미지 파일만 업로드할 수 있습니다. (JPG, JPEG, PNG, GIF)");
                $("#addForm").find("input[type='submit']").prop("disabled", true); // 제출 버튼 비활성화
            } else {
                hideError(errorId);
                $("#addForm").find("input[type='submit']").prop("disabled", false); // 제출 버튼 활성화
            }
        }
    }

    $("#imgurl1").change(function() {
        validateFile("imgurl1", "imgurlError1");
    });
	
    $("#imgurl2").change(function() {
        validateFile("imgurl2", "imgurlError2");
    });

    $("#imgurl3").change(function() {
        validateFile("imgurl3", "imgurlError3");
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

    // 입력 필드가 비어 있는지 확인하는 함수
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

    // 상품명 입력 필드에서 포커스를 잃었을 때 유효성 검사
    $("#name").blur(function() {
        checkEmptyInput("name", "nameError", "상품명을 입력해주세요.");
    });

    // 거래액 입력 필드에서 포커스를 잃었을 때 유효성 검사
    $("#price").blur(function() {
        checkEmptyInput("price", "priceError", "거래액을 입력해주세요.");
    });

    // 상품에 대한 설명 입력 필드에서 포커스를 잃었을 때 유효성 검사
    $("#description").blur(function() {
        checkEmptyInput("description", "descriptionError", "상품에 대한 설명을 입력해주세요.");
    });

    // 갯수 입력 필드에서 포커스를 잃었을 때 유효성 검사
    $("#count").blur(function() {
        checkEmptyInput("count", "countError", "갯수를 입력해주세요.");
    });

    // 카테고리 입력 필드에서 포커스를 잃었을 때 유효성 검사
    $("#category").blur(function() {
        checkEmptyInput("category", "categoryError", "카테고리를 입력해주세요.");
    });

    // 메이커 입력 필드에서 포커스를 잃었을 때 유효성 검사
    $("#maker").blur(function() {
        checkEmptyInput("maker", "makerError", "메이커를 입력해주세요.");
    });

    // 색상 입력 필드에서 포커스를 잃었을 때 유효성 검사
    $("#color").blur(function() {
        checkEmptyInput("color", "colorError", "색상을 입력해주세요.");
    });

    // 사이즈 입력 필드에서 포커스를 잃었을 때 유효성 검사
    $("#size").blur(function() {
        checkEmptyInput("size", "sizeError", "사이즈를 입력해주세요.");
    });

    // 기타옵션 입력 필드에서 포커스를 잃었을 때 유효성 검사
    $("#options").blur(function() {
        checkEmptyInput("options", "optionsError", "기타옵션을 입력해주세요.");
    });
});
</script>
</head>
<body>
<form id="addForm" action="/product/ADD" method="post" enctype="multipart/form-data">
    <label for="name">상품명:</label><br>
    <input type="text" id="name" name="name" placeholder="상품명을 입력해주세요"><br>
    <div id="nameError" class="error-message"></div><br>

    <label for="price">거래액:</label><br>
    <input type="text" id="price" name="price" placeholder="금액을 입력해주세요"><br>
    <div id="priceError" class="error-message"></div><br>

    <label for="description">상품에 대한 설명:</label><br>
    <input type="text" id="description" name="description" placeholder="입력해주세요"><br>
    <div id="descriptionError" class="error-message"></div><br>

    <label for="imgurl1">상품 이미지 1 :</label><br>
    <input type="file" id="imgurl1" name="imageFiles"><br>
    <div id="imgurlError1" class="error-message"></div><br>    

    <label for="imgurl2">상품 이미지 2 :</label><br>
    <input type="file" id="imgurl2" name="imageFiles"><br>
    <div id="imgurlError2" class="error-message"></div><br>    

    <label for="imgurl3">상품 이미지 3 :</label><br>
    <input type="file" id="imgurl3" name="imageFiles"><br>
    <div id="imgurlError3" class="error-message"></div><br> 

    <label for="count">갯수:</label><br>
    <input type="text" id="count" name="count" placeholder="수량을 입력해주세요"><br>
    <div id="countError" class="error-message"></div><br>

    <label for="category">카테고리:</label><br>
    <input type="text" id="category" name="category" placeholder="카테고리를 입력해주세요"><br>
    <div id="categoryError" class="error-message"></div><br>

    <label for="maker">메이커:</label><br>
    <input type="text" id="maker" name="maker" placeholder="메이커를 입력해주세요"><br>
    <div id="makerError" class="error-message"></div><br>
    
    <label for="color">색상:</label><br>
    <input type="text" id="color" name="color" placeholder="색상을 입력해주세요"><br>
    <div id="colorError" class="error-message"></div><br>

    <label for="size">사이즈:</label><br>
    <input type="text" id="size" name="size" placeholder="사이즈를 입력해주세요"><br>
    <div id="sizeError" class="error-message"></div><br>

    <label for="options">기타옵션:</label><br>
    <input type="text" id="options" name="options" placeholder="기타옵션을 입력해주세요"><br>
    <div id="optionsError" class="error-message"></div><br>
    
    <input type="submit" value="등록하기">
</form>

<c:if test="${not empty errorMessage}">
    <script>
        alert("${errorMessage}");
    </script>
</c:if>
</body>
</html>