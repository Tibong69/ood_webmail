<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko" xml:lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>비밀번호 변경</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script>
            <c:if test="${!empty msg}">
            alert("${msg}");
            </c:if>
        </script>
    </head>
    <body>
        <%@include file="header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>

        <div id="main">
            <form action="change_password" method="post">
                <table>
                    <tr>
                         <th colspan="2" style="text-align:center;">비밀번호 변경</th>
                    </tr>
                    <tr>
                        <td><label for="currentPassword">현재 비밀번호</label></td>
                        <td><input type="password" id="currentPassword" name="currentPassword" required></td>
                    </tr>
                    <tr>
                        <th scope="row"><label for="newPassword">새 비밀번호</label></th>
                        <td><input type="password" id="newPassword" name="newPassword" required></td>
                    </tr>
                    <tr>
                        <th scope="row"><label for="confirmPassword">새 비밀번호 확인</label></th>
                        <td><input type="password" id="confirmPassword" name="confirmPassword" required></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:center;">
                            <button type="submit">비밀번호 변경</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <%@include file="footer.jspf"%>
    </body>
</html>
