<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="lifelog:base" content="${lifelog.app.base}">
    <title>LifeLog</title>
    <link rel="stylesheet" href="${lifelog.app.css}/layout/home.css"/>
    <link rel="stylesheet" href="${lifelog.app.css}/layout/markdown.css"/>
</head>

<body>
<jsp:include page="${lifelog.app.jsp}/layout/top.jsp"/>
<div class="page">
    <!-- 좌측 메뉴 -->
    <aside class="sidebar-wrap">
        <jsp:include page="${lifelog.app.jsp}/layout/leftMenu.jsp"/>
    </aside>

    <main class="content">
        <div id="md-template" class="mdtpl">
            <div class="mdtpl__toolbar">
                <input id="tplName" class="mdtpl__name" maxlength="30" placeholder="템플릿 이름 (예: 오늘 할일)" />
                <div class="mdtpl__spacer"></div>
                <select id="tplLoad" class="mdtpl__btn">
                    <option value="">저장된 템플릿 불러오기…</option>
                </select>
                <button id="tplSave"   class="mdtpl__btn">저장</button>
            </div>

            <div class="mdtpl__grid">
                <div class="mdtpl__left">
                    <label class="mdtpl__label">템플릿 (마크다운)</label>
                    <textarea id="tplText" class="mdtpl__text" rows="12" placeholder="여기에 템플릿을 작성하세요. 예시:
오늘 할일
- $1
- $2
- $3

메모: $?
"></textarea>

                    <div class="mdtpl__params">
                        <div class="mdtpl__paramsHead">
                            <span>파라미터</span>
                            <small class="mdtpl__hint">$1, $2 … 또는 $? 같은 자리표시자를 템플릿에 쓰세요.</small>
                        </div>
                        <div id="paramList" class="mdtpl__paramList">

                        </div>
                    </div>
                </div>

                <div class="mdtpl__right">
                    <div class="mdtpl__previewHead">
                        <span>미리보기</span>
                        <button id="copyRendered" class="mdtpl__btn">렌더된 텍스트 복사</button>
                    </div>
                    <div id="preview" class="mdtpl__preview markdown-body">

                    </div>
                </div>
            </div>
        </div>

    </main>
</div>

<!-- 푸터 -->
<jsp:include page="${lifelog.app.jsp}/layout/footer.jsp"/>
</body>
</html>

<!-- Markdown Parser & XSS 세이프가드 -->
<script src="${lifelog.app.script.marked}"></script>
<script src="${lifelog.app.script.safeGuard}"></script>

<script src="${lifelog.app.js}/layout/home.js"></script>
<script src="${lifelog.app.js}/common/common.js"></script>