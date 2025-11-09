<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>게시글 목록 - LifeLog</title>
    <link rel="stylesheet" href="${lifelog.app.css}/layout/home.css"/>
    <link rel="stylesheet" href="${lifelog.app.css}/post/post.css"/>
</head>
<body>
<jsp:include page="${lifelog.app.jsp}/layout/top.jsp"/>
<div class="page">
    <aside class="sidebar-wrap">
        <jsp:include page="${lifelog.app.jsp}/layout/leftMenu.jsp"/>
    </aside>

    <main class="content post-list-page">
        <section class="post-list__search">
            <form id="postSearchForm" method="get" action="#" class="post-search-form">
                <input
                        type="text"
                        name="q"
                        placeholder="검색어를 입력하세요"
                        class="post-search__input" />

                <button type="submit" class="post-search__btn">검색</button>

                <c:if test="${not empty lifeLogUser}">
                    <label class="post-search__my-toggle">
                            <input type="checkbox" name="my" value="true" />
                            <span>내 게시글만 보기</span>
                    </label>
                </c:if>
            </form>
        </section>

        <section class="post-list__table-wrap">
            <table class="post-table">
                <thead>
                <tr>
                    <th style="width:70px;">번호</th>
                    <th>제목</th>
                    <th style="width:120px;">작성자</th>
                    <th style="width:150px;">작성일</th>
                    <th style="width:80px;">조회수</th>
                </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty postList}}">
                            <tr>
                                <td colspan="5" class="post-table__empty">
                                    등록된 게시글이 없습니다.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="post" items="${postList}" varStatus="no">
                                <tr class="post-row">
                                    <td class="post-col__id">${post.id}</td>
                                    <td class="post-col__title">
                                        <span class="post-title">${post.title}</span>
                                    </td>
                                    <td class="post-col__author">${post.userInfo.username}</td>
                                    <td class="post-col__date">
                                        <fmt:formatDate value="${post.getCreatedTypeDate()}" pattern="yyyy.MM.dd" />
                                    </td>
                                    <td class="post-col__views">0</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </section>

        <section class="post-list__bottom">
            <ul class="pagination">
                <li class="page-item">
                    <a href="#" class="page-link">이전</a>
                </li>
                <li class="page-item active">
                    <a href="#" class="page-link">1</a>
                </li>
                <li class="page-item">
                    <a href="#" class="page-link">2</a>
                </li>
                <li class="page-item">
                    <a href="#" class="page-link">3</a>
                </li>
                <li class="page-item">
                    <a href="#" class="page-link">다음</a>
                </li>
            </ul>

            <div class="post-list__actions">
                <c:if test="${not empty lifeLogUser}">
                    <a href="${lifelog.app.base}/post/write" class="btn-write">글쓰기</a>
                </c:if>
            </div>
        </section>

    </main>
</div>

<jsp:include page="${lifelog.app.jsp}/layout/footer.jsp"/>

</body>
</html>
