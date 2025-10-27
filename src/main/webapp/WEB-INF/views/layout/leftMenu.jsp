<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="uri" value="${pageContext.request.requestURI}"/>
<nav class="nav">
    <a href="${pageContext.request.contextPath}/" class="${fn:contains(uri,'/') && !fn:contains(uri,'/posts') ? 'active' : ''}">대시보드</a>
    <a href="${pageContext.request.contextPath}/posts" class="${fn:contains(uri,'/posts') ? 'active' : ''}">게시글</a>
    <a href="${pageContext.request.contextPath}/settings" class="${fn:contains(uri,'/settings') ? 'active' : ''}">설정</a>
</nav>
