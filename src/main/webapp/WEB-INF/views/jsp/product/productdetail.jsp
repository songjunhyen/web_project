<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.example.demo.vo.Product"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>제품 상세보기</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $(document).ready(function(){
        loadReviews();
        loadAverageStar();

        function loadReviews() {
            $.get("/Review/list", { productid: ${product.id} }, function(data) {
                let reviewsHtml = '';
                $.each(data, function(index, review) {
                    reviewsHtml += '<div class="py-2 border-bottom-line pl-16">';
                    reviewsHtml += '<div class="text-m my-1 ml-2">' + review.writer + '</div>';
                    reviewsHtml += '<div class="text-m my-1 ml-2">' + review.reviewText + '</div>';
                    reviewsHtml += '<div class="text-xs text-gray-400">' + review.regDate + '</div>';
                    if (review.writer == '${sessionScope.userid}') {
                        reviewsHtml += '<div id="modifyForm">';
                        reviewsHtml += '<form action="/Review/modify" method="post" onsubmit="return confirm(\'정말 수정하시겠습니까?\');">';
                        reviewsHtml += '<input type="hidden" name="productid" value="' + ${product.id} + '" />';
                        reviewsHtml += '<input type="hidden" name="reviewid" value="' + review.id + '" />';
                        reviewsHtml += '<textarea class="textarea textarea-bordered textarea-lg w-full" name="body" placeholder="수정 리뷰를 입력해주세요"></textarea>';
                        reviewsHtml += '<div class="mt-4 reply-border p-4">';
                        reviewsHtml += '<div class="flex justify-end">';
                        reviewsHtml += '<button type="submit" class="btn btn-active btn-sm">수정하기</button>';
                        reviewsHtml += '</div></div></form></div>';
                        reviewsHtml += '<form action="/Review/delete" method="post" onsubmit="return confirm(\'정말 삭제하시겠습니까?\');">';
                        reviewsHtml += '<input type="hidden" name="productid" value="' + ${product.id} + '" />';
                        reviewsHtml += '<input type="hidden" name="reviewid" value="' + review.id + '" />';
                        reviewsHtml += '<div class="mt-4 reply-border p-4">';
                        reviewsHtml += '<div class="flex justify-end">';
                        reviewsHtml += '<button class="btn btn-active btn-sm">삭제</button>';
                        reviewsHtml += '</div></div></form>';
                    }
                    reviewsHtml += '</div>';
                });
                $("#reviewsContainer").html(reviewsHtml);
            });
        }

        function loadAverageStar() {
            $.get("/Review/getstar", { productid: ${product.id} }, function(data) {
                $("#averageStar").text(data.toFixed(1));
            });
        }

        $("#addReviewForm").on("submit", function(event) {
            event.preventDefault();
            $.post("/Review/add", $(this).serialize(), function() {
                loadReviews();
                loadAverageStar();
            });
        });
    });
</script>
</head>
<body>
	<div class="table-box-type">
		<table>
			<thead>
				<tr>
					<th>번호</th>
					<th>카테고리</th>
					<th>제품명</th>
					<th>금액</th>
					<th>제조사</th>
					<th>색상</th>
					<th>사이즈</th>
					<th>제품설명</th>
					<th>조회수</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>${product.id}</td>
					<td>${product.category}</td>
					<td>${product.name}</td>
					<td>${product.price}</td>
					<td>${product.maker}</td>
					<td>${product.color}</td>
					<td>${product.size}</td>
					<td>${product.description}</td>
					<td>${product.viewcount}</td>
					<td>${product.regDate}</td>
				</tr>
			</tbody>
		</table>

		<div class="mt-3">
			<h3>
				평균 별점: <span id="averageStar">0.0</span>
			</h3>
		</div>

		<section>
			<div class="container mx-auto px-3">
				<div class="text-lg">리뷰</div>
				<div id="reviewsContainer"></div>
			</div>
		</section>

		<div class="mt-3">
			<h3>리뷰 작성</h3>
			<form action="/Review/add" method="post">
				<input type="hidden" name="productId" value="${product.id}" />
				<textarea name="body" placeholder="리뷰를 입력하세요"></textarea>
				<input type="number" name="star" step="0.5" min="0" max="5"
					placeholder="별점 (0.0 - 5.0)" />
				<button type="submit">작성하기</button>
			</form>
		</div>

		<div class="mt-3 text-sm">
			<button class="btn btn-active btn-sm" onclick="location.href='/product/list';">목록으로</button>
						
			<form action="/product/modify" method="get">
				<input type="hidden" name="id" value="${product.id}">
				<button type="submit">수정</button>
			</form>
			<form action="/product/delete" method="post">
				<input type="hidden" name="id" value="${product.id}">
				<button onclick="if(confirm('정말 삭제하시겠습니까?') == false) return false;">삭제</button>
			</form>
		</div>
	</div>
</body>
</html>