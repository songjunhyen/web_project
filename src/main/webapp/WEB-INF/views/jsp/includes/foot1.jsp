<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<style>
        #siteFooter {
            background-color: #333;
            color: white;
            padding: 10px;
            text-align: center;
            position: fixed; /* footer를 화면 하단에 고정 */
            bottom: 0;
            width: 100%;
            height: 60px; /* footer의 높이 설정 */
        }

        #siteFooter ul {
            margin: 0;
            padding: 0;
            list-style: none;
        }

        #siteFooter li {
            display: inline;
            margin: 0 10px;
        }

        #siteFooter a {
            color: white;
            text-decoration: none;
        }

        #siteFooter a:hover {
            text-decoration: underline;
        }

        #footerText {
            margin-top: 10px;
        }
</style>

<!-- Footer -->
<footer id="siteFooter">
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
    <div id="footerText">상호명 고객센터 연락처 안내문 등</div>
</footer>

</body>
</html>