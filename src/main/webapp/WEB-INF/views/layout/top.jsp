<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<link rel="stylesheet" href="${lifelog.app.css}/layout/top.css"/>

<script src="${lifelog.app.script.jquery}"></script>

<script src="${lifelog.app.js}/layout/top.js"></script>

<div class="topbar">
    <button class="hamburger"
            type="button"
            data-menu-toggle
            aria-controls="sidebar"
            aria-expanded="false"
            aria-label="메뉴 열기"
            style="background:#ff4757;border:0;color:#fff;padding:0 10px;border-radius:8px;">☰</button>
    <div class="brand"><a href="/">LifeLog</a></div>
    <div style="flex:1"></div>
    <div>
        <c:choose>
            <c:when test="${not empty lifeLogUser}">
                <div class="userbox">
                    <img class="avatar" src="${lifeLogUser.picture()}" alt="${lifeLogUser.name()}" referrerpolicy="no-referrer"/>
                    <span class="username"><c:out value="${lifeLogUser.name()}"/></span>
                    <form action="${lifelog.app.base}/logout" method="post" style="display:inline">
                        <button type="submit" class="logout">Logout</button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <a href="${lifelog.app.base}/auth/login" style="color:#fff">Login</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- 어둡게 가리는 오버레이 -->
<div class="backdrop" data-menu-close aria-hidden="true"></div>
