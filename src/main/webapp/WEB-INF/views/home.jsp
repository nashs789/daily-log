<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>DailyLog</title>
    <link rel="stylesheet" href="/css/layout/home.css"/>
</head>
<body>
<!-- 상단바 -->
<jsp:include page="/WEB-INF/views/layout/top.jsp"/>

<div class="page">
    <!-- 좌측 메뉴 -->
    <aside class="sidebar-wrap">
        <jsp:include page="/WEB-INF/views/layout/leftMenu.jsp"/>
    </aside>

    <!-- 메인 컨텐츠 -->
    <main class="content">
        <h1>대시보드</h1>
        <p>여기에 메인 콘텐츠를 배치하세요.</p>
    </main>
</div>

<!-- 푸터 -->
<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>
