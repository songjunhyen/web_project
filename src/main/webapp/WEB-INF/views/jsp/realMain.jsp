<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Main</title>
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.6.3/css/all.css"
	crossorigin="anonymous">
<%@ include file="includes/head1.jsp"%>


<style>
#main {
	
}

#productContainer {
	width: 80%;
	margin: 30px auto;
	text-align: center;
}

#productTable {
	width: 100%;
	border-collapse: collapse;
}

#productTable th, #productTable td {
	border: 1px solid #ddd;
	padding: 8px;
}

#productTable th {
	background-color: #f2f2f2;
	font-weight: bold;
}

#productTable td button {
	background: none;
	border: none;
	color: blue;
	cursor: pointer;
	text-decoration: underline;
}

#pagination {
	margin-top: 20px;
}

#pagination a {
	margin: 0 5px;
	text-decoration: none;
	color: blue;
}

#pagingContainer {
	text-align: right;
	margin-top: 20px;
	margin: 0 2px;
}

#pagingContainer a, #pagingContainer span {
	width: 20px;
	display: inline-block;
	margin: 0 4px;
	padding: 3px 1px;
	text-decoration: none;
	color: black;
	border: 1px solid #ddd;
	border-radius: 5px;
	text-align: center;
}

#pagingContainer #pagingbotton {
	width: 35px;
}

#pagingContainer a:hover {
	background-color: #ddd;
}

#pagingContainer span {
	font-weight: bold;
}

.container, .content, .slider img, .header, .footer {
    box-sizing: border-box;
}
img, input, button, .slider-container {
    box-sizing: border-box;
}

/* 슬라이드 컨테이너 스타일 */
.slideshow-container {
    position: relative;
    max-width: 90%;
    margin: auto;
    height: 100%;    
}

/* 슬라이드 이미지 스타일 */
.main_slideImg {
    height: 80%;   
    width: 100%; 
    object-fit: cover;
    padding : 0 20%;
}

/* 이전/다음 버튼 스타일 */
.prev, .next {
    cursor: pointer;
    position: absolute;
    top: 50%;
    width: 2%;
    padding: 10px;
    color: white;
    font-weight: bold;
    font-size: 20px;
    transition: 0.6s ease;
    border-radius: 0 3px 3px 0;
    z-index: 100;
    text-align: center;
    transform: translateY(-50%);    
    background-color: rgba(0, 0, 0, 0.3);
}

.next {
    right: 0;
    border-radius: 3px 0 0 3px;
}

.prev:hover, .next:hover {
    background-color: rgba(0, 0, 0, 0.8);
}

/* 캡션 텍스트 스타일 */
.text {
    color: #f2f2f2;
    font-size: 15px;
    padding: 8px 12px;
    position: absolute;
    bottom: 8px;
    width: 100%;
    text-align: center;   
}

/* 슬라이드 인디케이터 스타일 */
.dot-container {
    text-align: center;    
}

.dot {
    cursor: pointer;
    height: 13px;
    width: 13px;
    margin: 0 2px;
    background-color: #bbb;
    border-radius: 50%;
    display: inline-block;
    transition: background-color 0.6s ease;
}

.active, .dot:hover {
    background-color: #717171;
}

/* 페이드 애니메이션 */
.fade2 {
    animation-name: fade;
    animation-duration: 1s;
}

@keyframes fade {
    from { opacity: 0.4; }
    to { opacity: 1; }
}

/* 작은 화면에서 텍스트 크기 조정 */
@media only screen and (max-width: 300px) {
    .prev, .next, .text {
        font-size: 11px;
    }
}
</style>
</head>
<body id="main">
	<br>
<div class="slideshow-container">
    <div class="mySlides fade2">
        <img class="main_slideImg" src="/event/banner1.jpg" alt="Slide 1">
        <div class="text">Event one</div>
    </div>
    <div class="mySlides fade2">
        <img class="main_slideImg" src="/event/banner2.jpg" alt="Slide 2">
        <div class="text">Event Two</div>
    </div>
    <div class="mySlides fade2">
        <img class="main_slideImg" src="/event/banner3.jpg" alt="Slide 3">
        <div class="text">Event Three</div>
    </div>
    <a class="prev" onclick="plusSlides(-1)">❮</a>
    <a class="next" onclick="plusSlides(1)">❯</a>
</div>

<div class="dot-container">
    <span class="dot" onclick="currentSlide(1)"></span>
    <span class="dot" onclick="currentSlide(2)"></span>
    <span class="dot" onclick="currentSlide(3)"></span>
</div>	
	<br>

	<div id="productContainer">
		<table id="productTable">
			<thead>
				<tr>
					<th></th>
					<th>번호</th>
					<th>카테고리</th>
					<th>제품명</th>
					<th>금액</th>
					<th>조회수</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="product" items="${products}">
					<tr>
						<td>
							<h3>제품 이미지</h3> <c:if test="${not empty product.imageUrl}">
								<c:set var="imageUrls"
									value="${fn:split(product.imageUrl, ',')}" />
								<c:if test="${fn:length(imageUrls) > 0}">
									<img src="${imageUrls[0]}" alt="Product Image"
										style="max-width: 300px;" />
								</c:if>
							</c:if> <c:if test="${empty product.imageUrl}">
								<p>이미지가 없습니다.</p>
							</c:if>
						</td>
						<td>${product.id}</td>
						<td>${product.category}</td>
						<td>
							<form action="/product/Detail" method="post">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}"> <input type="hidden" name="id"
									value="${product.id}">
								<button type="submit">${product.name}</button>
							</form>
						</td>
						<td>${product.price}</td>
						<td>${product.viewcount}</td>
						<td>${product.regDate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<%-- 페이징 버튼 --%>
	<div id="pagingContainer">
		<c:if test="${currentPage != null && totalPages != null}">
			<c:if test="${currentPage > 1}">
				<a href="?page=${currentPage - 1}" id="pagingbotton">이전</a>
			</c:if>

			<c:forEach var="i" begin="${startPage}" end="${endPage}">
				<c:choose>
					<c:when test="${i == currentPage}">
						<span>${i}</span>
					</c:when>
					<c:otherwise>
						<a href="?page=${i}">${i}</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>

			<c:if test="${currentPage < totalPages}">
				<a href="?page=${currentPage + 1}" id="pagingbotton">다음</a>
			</c:if>

			<br>

			<p>현재 페이지 범위: ${startPage} - ${endPage} (총 페이지: ${totalPages})</p>
		</c:if>
	</div>

	<br>
	<div id="proContainer">인기상품 들어갈 공간</div>

	<br>
	<div id="articleContainer">
		공지사항 등 들어갈 공간
		<table id="articletable">
			<thead id="article">
				<tr>
					<th></th>
					<th>번호</th>
					<th>제목</th>
					<th>조회수</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="article" items="${articles}">
					<tr>
						<td>${article.id}</td>
						<td>${article.boardid}</td>
						<td>
							<form action="/article/Detail" method="post">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}"> <input type="hidden" name="id"
									value="${article.id}">
								<button type="submit">${article.name}</button>
							</form>
						</td>
						<td>${article.viewcount}</td>
						<td>${article.regDate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<%@ include file="includes/foot1.jsp"%>

	<script>

	function autoSlide() {
        plusSlides(1);
        setTimeout(autoSlide, 4000); // 4초마다 이동 
    }
	
		var slideIndex = 1;
		showSlides(slideIndex);
		function plusSlides(n) {
			showSlides(slideIndex += n);
		}
		function currentSlide(n) {
			showSlides(slideIndex = n);
		}
		function showSlides(n) {
			var i;
			var slides = document.getElementsByClassName("mySlides");
			var dots = document.getElementsByClassName("dot");
			if (n > slides.length) {
				slideIndex = 1
			}
			if (n < 1) {
				slideIndex = slides.length
			}
			for (i = 0; i < slides.length; i++) {
				slides[i].style.display = "none";
			}
			for (i = 0; i < dots.length; i++) {
				dots[i].className = dots[i].className.replace(" active", "");
			}
			slides[slideIndex - 1].style.display = "block";
			dots[slideIndex - 1].className += " active";
		}
		
		
		showSlides(slideIndex);
		autoSlide();
		
	</script>

</body>
</html>
