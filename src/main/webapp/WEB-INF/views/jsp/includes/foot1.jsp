<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<style>
    body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }

    footer {
        display: flex;
        justify-content: flex-end; /* 우측 정렬 */
        background-color: #333;
        padding: 10px;
    }

    footer ul {
        display: flex;
        list-style: none; /* 목록 스타일 제거 */
        padding: 0;
        margin: 0;
    }

    footer li {
        color: white;
        margin-left: 20px;
    }

    footer a {
        color: white;
        text-decoration: none;
        padding: 10px;
    }

    footer a:hover {
        background-color: #333;
        color: white;
    }

    #text {
        color: white;
        margin-left: 20px;
        padding: 10px;
    }
</style>

<!-- Footer -->
<footer>
    <ul>
        <li><a href="#">소개</a></li>
        <li>|</li>
        <li><a href="#">개인정보 처리 방침</a></li>
        <li>|</li>
        <li><a href="#">이용약관</a></li>
        <li>|</li>
        <li><a href="#">입점/제휴 문의</a></li>
        <li>|</li>
        <li><a href="#">고객지원</a></li>
    </ul>
    <br>
    <div id="text">상호명 고객센터 연락처 안내문 등</div>
</footer>

</body>
</html>