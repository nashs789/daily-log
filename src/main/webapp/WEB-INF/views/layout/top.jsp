<%@ page contentType="text/html; charset=UTF-8" %>
<div class="topbar">
    <!-- 메뉴 토글 -->
    <button class="hamburger" type="button"
            data-menu-toggle
            aria-controls="sidebar"
            aria-expanded="false"
            aria-label="메뉴 열기">
        ☰
    </button>

    <div class="brand"><a href="${pageContext.request.contextPath}/">LifeLog</a></div>
    <div style="flex:1"></div>
    <div>
        <a href="${pageContext.request.contextPath}/oauth2/authorization/google" style="color:#fff">Login</a>
    </div>
</div>
