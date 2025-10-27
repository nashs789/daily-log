<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>LifeLog</title>
    <!-- 정적 CSS: src/main/resources/static/css/app.css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css"/>
    <style>
        body{margin:0;background:#f7f7f9;font-family:system-ui,-apple-system,Segoe UI,Roboto}
        .topbar{height:56px;display:flex;align-items:center;padding:0 16px;background:#111;color:#fff}
        .topbar .brand a{color:#fff;text-decoration:none;font-weight:700}
        .page{display:grid;grid-template-columns:240px 1fr;min-height:calc(100vh - 56px - 48px)}
        .sidebar{background:#fff;border-right:1px solid #eee}
        .content{padding:24px}
        .footer{height:48px;display:flex;align-items:center;justify-content:center;color:#777;background:#fff;border-top:1px solid #eee}
        .nav a{display:block;padding:10px 14px;color:#222;text-decoration:none;border-radius:8px;margin:4px 8px}
        .nav a.active{background:#eef2ff;color:#2b50ff}
    </style>
</head>
<body>
<!-- 상단바 -->
<jsp:include page="/WEB-INF/views/layout/top.jsp"/>

<div class="page">
    <!-- 좌측 메뉴 -->
    <aside class="sidebar">
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
