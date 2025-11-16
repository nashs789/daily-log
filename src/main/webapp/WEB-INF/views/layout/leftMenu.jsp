<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav id="sidebar" class="sidebar" aria-label="사이드 메뉴">
    <div class="sidebar__scroll">
        <a class="nav-item" href="${lifelog.app.base}/">대시보드</a>
        <a class="nav-item" href="${lifelog.app.base}/post">게시글</a>
        <c:if test="${not empty lifeLogUser}">
            <a class="nav-item" href="${lifelog.app.base}/webhook/history">전송 이력</a>
        </c:if>
    </div>
</nav>
