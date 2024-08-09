<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 수정</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <style>
		 .password-field {
            position: relative;
            display: flex;
            align-items: center;            
        }
        .password-field input.adminPw {
            padding-right: 10%; /* 버튼 공간 확보 */
            box-sizing: border-box; /* 패딩을 포함하여 총 너비 조정 */
            overflow: hidden;
            text-overflow: ellipsis; 
        }
        .password-field .toggle-password {
            position: absolute;
            right: 0;
            top: 0;
            cursor: pointer;
            padding: 5px;
            background-color: #f0f0f0;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 12px;
            color: #333;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
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
            
            // 비밀번호 초기화 버튼 클릭 이벤트
            $(document).on('click', '.reset-AD', function(event) {
                event.preventDefault();
                var adminid = $(this).data('adminid');
                resetAD(adminid);
            });

            // 비밀번호 보기 버튼 클릭 이벤트
            $(document).on('click', '.toggle-password', function() {
                var input = $(this).siblings('.adminPw');
                var type = input.attr('type') === 'password' ? 'text' : 'password';
                input.attr('type', type);
                $(this).text(type === 'text' ? '숨기기' : '보기');
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
                        table += '<thead><tr><th>클래스</th><th>관리자ID</th><th>관리자PW</th><th>이름</th><th>이메일</th><th>ID PW초기화</th></tr></thead><tbody>';
                        $.each(data, function(index, admin) {
                            table += '<tr><td>' + admin.adminclass + '</td>';
                            table += '<td>' + admin.adminId + '</td>';
                            table += '<td class="password-field"><input type="password" class="adminPw" value="' + admin.adminPw + '" readonly /> <button class="toggle-password">보기</button></td>';
                            table += '<td>' + admin.name + '</td>';
                            table += '<td>' + admin.email + '</td>';
                            table += '<td><button class="reset-AD" data-adminid="' + admin.adminId + '">초기화</button></td></tr>';
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

        function resetAD(adminid) {
            $.ajax({
                url: '/admin/resetAD', // 비밀번호 초기화 요청을 처리하는 URL
                type: 'POST',
                data: {
                    adminid: adminid // adminid 변수명 수정
                },
                beforeSend: function(xhr) {
                    // CSRF 토큰을 헤더에 추가
                    if (csrfToken && csrfHeader) {
                        xhr.setRequestHeader(csrfHeader, csrfToken);
                    }
                },
                success: function(response) {
                    alert('초기화되었습니다.');
                    performSearch(); // 비밀번호 초기화 후 검색 결과를 갱신
                },
                error: function(e) {
                    console.error("초기화 중 오류가 발생했습니다:", e);
                    alert('초기화 중 오류가 발생했습니다.');
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