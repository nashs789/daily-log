<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>로그인</title>
    <link rel="stylesheet" href="${lifelog.app.css}/auth/login.css"/>
</head>
<body>
<div class="wrap">
    <main class="main">
        <div class="card">
            <div class="grid">
                <!-- 왼쪽: 기본 로그인 (지금은 UI만; 추후 구현) -->
                <div class="col">
                    <h2 class="h">이메일 로그인</h2>
                    <p class="muted">ID와 비밀번호로 로그인하세요. (나중에 구현 예정)</p>

                    <form method="post" action="#" onsubmit="alert('아직 준비 중입니다. 소셜 로그인을 사용해주세요.');return false;">
                        <div class="row field">
                            <label>아이디</label>
                            <input class="input" type="text" name="username" placeholder="이메일 또는 아이디"/>
                        </div>
                        <div class="row field">
                            <label>비밀번호</label>
                            <input class="input" type="password" name="password" placeholder="비밀번호"/>
                        </div>
                        <div class="row" style="display:flex; gap:12px; justify-content:space-between; align-items:center">
                            <div>
                                <button type="button" class="btn link" onclick="alert('아이디 찾기는 나중에 연결됩니다.')">아이디 찾기</button>
                                <button type="button" class="btn link" onclick="alert('비밀번호 찾기는 나중에 연결됩니다.')">비밀번호 찾기</button>
                            </div>
                            <button type="button" class="btn light" onclick="alert('회원가입은 나중에 연결됩니다.')">회원가입</button>
                        </div>
                        <div class="row"><button type="submit" class="btn">로그인</button></div>
                    </form>

                    <p class="kicker">* 자체 로그인은 이후에 연결할 예정입니다.</p>
                </div>

                <!-- 오른쪽: 소셜 로그인 -->
                <div class="col" style="border-left:1px solid #eee">
                    <h2 class="h">소셜 로그인</h2>
                    <p class="muted">원하는 계정으로 빠르게 로그인하세요.</p>

                    <div class="social">
                        <!-- 구글 로그인 -->
                        <c:url value="${lifelog.app.base}/oauth2/authorization/google" var="googleUrl"/>
                        <!-- 필요 시 모바일 딥링크를 함께 넘겨야 한다면 아래처럼 쿼리에 추가 -->
                        <%-- <c:url value="/oauth2/authorization/google" var="googleUrl"><c:param name="appScheme" value="lifelog://oauth"/></c:url> --%>

                        <a class="btn light" style="display:flex; align-items:center; gap:8px; justify-content:center; text-decoration:none"
                           href="${googleUrl}">
                            <img src="${lifelog.app.image}/google.svg" alt="" width="18" height="18"/>
                            Google로 로그인
                        </a>

                        <!-- (미리보기용) 네이버/카카오는 나중에 client 등록 후 활성화 -->
                        <button class="btn light" disabled style="opacity:.6">Naver (준비중)</button>
                        <button class="btn light" disabled style="opacity:.6">Kakao (준비중)</button>
                    </div>
                </div>
            </div>

            <div class="split"></div>
            <div class="col" style="display:flex; justify-content:center; gap:18px">
                <a href="${lifelog.app.base}/" class="btn light">메인으로</a>
            </div>
        </div>
    </main>

    <jsp:include page="${lifelog.app.jsp}/layout/footer.jsp"/>
</div>
</body>
</html>
