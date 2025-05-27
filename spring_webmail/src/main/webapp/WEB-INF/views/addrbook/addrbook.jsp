<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko" xml:lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>주소록</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script>
            <c:if test="${!empty msg}">
            alert("${msg}");
            </c:if>
        </script>
    </head>
    <body>
        <%@include file="../header.jspf"%>

        <div id="sidebar">
            <jsp:include page="../sidebar_previous_menu.jsp" />
        </div>

        <div id="main">
            <h2>주소록</h2>
            <hr/>
            <table border="1" style="width: 70%; margin: 0 auto;">
                <tr>
                    <th>이름</th>
                    <th>이메일</th>
                </tr>
                <c:forEach var="row" items="${dataRows}">
                    <tr>
                        <td>${row.name}</td>
                        <td>${row.email}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty dataRows}">
                    <tr><td colspan="2">주소록이 비어있습니다.</td></tr>
                </c:if>
            </table>
            <br>
            <a href="${pageContext.request.contextPath}/addrbook/insert">주소록 추가</a>
        </div>

        <%@include file="../footer.jspf"%>
    </body>
</html>
