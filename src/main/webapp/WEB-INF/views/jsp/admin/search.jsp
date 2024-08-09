<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 검색</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <script>
        var csrfToken;
        var csrfHeader;

        $(document).ready(function() {
            csrfToken = $('meta[name="_csrf"]').attr('content');
            csrfHeader = $('meta[name="_csrf_header"]').attr('content');

            $("#searchbutton").click(function(event) {
                event.preventDefault(); // 폼 제출 방지
                performSearch();
            });
        });

        function performSearch() {
            var adminclass = $("select[name='adminclass']").val().trim();
            var name = $("#name").val().trim();
            var email = $("#email").val().trim();
            
            $.ajax({
                url: 'Searching', // 서버에서 결과를 반환하는 URL
                type: 'POST',
                data: {
                    adminclass: adminclass,
                    name: name,
                    email: email
                },
                beforeSend: function(xhr) {
                    // CSRF 토큰을 헤더에 추가
                    if (csrfToken && csrfHeader) {
                        xhr.setRequestHeader(csrfHeader, csrfToken);
                    }
                },
                success: function(data) {
                    // 검색 결과를 리스트에 출력
                    var resultDiv = $('#searchResults');
                    resultDiv.empty(); // 이전 결과 삭제
                    if (data.length > 0) {
                        var table = '<table border="1" cellpadding="5" cellspacing="0" style="width:100%">';
                        table += '<thead><tr><th>클래스</th><th>이름</th><th>이메일</th></tr></thead><tbody>';
                        $.each(data, function(index, admin) {
                            table += '<tr><td>' + admin.adminclass + '</td><td>' + admin.name + '</td><td>' + admin.email + '</td></tr>';
                        });
                        table += '</tbody></table>';
                        resultDiv.html(table);
                    } else {
                        resultDiv.html('<p>검색 결과가 없습니다.</p>');
                    }
                },
                error: function(e) {
                    console.error("검색 중 오류가 발생했습니다:", e);
                    $('#searchResults').html('<p>검색 중 오류가 발생했습니다.</p>');
                }
            });
        }
    </script>
</head>
<%@ include file="../includes/head1.jsp"%>
<body>

<div>
    <form id="searchForm">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <label for="adminclass">클래스:</label>
        <select name="adminclass">
            <option value="">전체</option>
            <option value="2">고객지원팀</option>
            <option value="3">사용자 관리팀</option>
            <option value="4">결제문의팀</option>
            <option value="5">상품관리팀</option>
            <option value="6">기타</option>
        </select>
        <br>
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" placeholder="이름을 입력해주세요" />
        <br>
        <label for="email">이메일:</label>
        <input type="text" id="email" name="email" placeholder="이메일을 입력해주세요" />
        <br>
        <button id="searchbutton" type="submit">Search</button>
    </form>
</div>
<br><br><br>
<div id="searchResults">
    <!-- 검색 결과가 여기에 표시됩니다 -->
</div>
<br>
<br>
</body>
<%@ include file="../includes/foot1.jsp"%>
</html>