<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko" xml:lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>주소록 추가</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/main_style.css" />
        <script>
            <c:if test="${!empty msg}">
            alert("${msg}");
            </c:if>
        </script>
    </head>
    <body>
        <%@include file="/WEB-INF/views/header.jspf"%>

        <div id="sidebar">
            <jsp:include page="/WEB-INF/views/sidebar_previous_menu.jsp" />
        </div>

        <div id="main">
            <h2>주소록 추가</h2>
            <hr/>
            <form action="${pageContext.request.contextPath}/addrbook/insert" method="POST">
                <table style="margin: 0 auto;">
                    <tr>
                        <th colspan="2" style="text-align:center;">사용자 정보 입력</th>
                    </tr>
                    <tr>
                        <td>이름</td>
                        <td><input type="text" name="name" size="20" required /></td>
                    </tr>
                    <tr>
                        <td>이메일</td>
                        <td><input type="email" name="email" size="30" required /></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:center;">
                            <input type="submit" value="추가" />
                            <input type="reset" value="초기화" />
                        </td>
                    </tr>
                </table>
            </form>
            <br>
            <a href="${pageContext.request.contextPath}/addrbook">목록으로</a>
        </div>

        <%@include file="/WEB-INF/views/footer.jspf"%>
    </body>
</html>
