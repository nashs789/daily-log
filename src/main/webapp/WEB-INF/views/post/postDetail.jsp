<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>게시글 상세 - LifeLog</title>
    <link rel="stylesheet" href="${lifelog.app.css}/layout/home.css"/>
    <link rel="stylesheet" href="${lifelog.app.css}/layout/markdown.css"/>
    <link rel="stylesheet" href="${lifelog.app.css}/post/postDetail.css"/>
</head>
<body>
<!-- 상단바 -->
<jsp:include page="${lifelog.app.jsp}/layout/top.jsp"/>

<div class="page">
    <!-- 좌측 메뉴 -->
    <aside class="sidebar-wrap">
        <jsp:include page="${lifelog.app.jsp}/layout/leftMenu.jsp"/>
    </aside>

    <main class="content">
        <section class="post-detail">
            <header class="post-detail__header">
                <div class="post-detail__title-row">
                    <h1 class="post-detail__title">${post.title}</h1>
                    <c:if test="${not empty lifeLogUser}">
                        <div class="post-detail__actions">
                            <button type="button" class="btn btn--danger" id="btnDelete" data-post-id="${post.id}">삭제</button>
                            <a href="${lifelog.app.base}/post/${post.id}/edit" class="btn btn--ghost">수정</a>
                        </div>
                    </c:if>
                </div>

                <div class="post-detail__meta">
                    <span class="post-detail__author">작성자: ${post.userInfo.username}</span>
                    <span class="post-detail__dot">·</span>
                    <span class="post-detail__date"><fmt:formatDate value="${post.getCreatedTypeDate()}" pattern="yyyy.MM.dd" /></span>
                    <span class="post-detail__dot">·</span>
                    <span class="post-detail__views">조회 0</span>
                </div>
            </header>

            <div class="post-detail__body">
                <article class="post-detail__content markdown-body">${post.content}</article>

                <aside class="post-detail__template">
                    <div class="template-card">
                        <div class="template-card__header">
                            <div class="template-card__head-left">
                                <span class="template-card__title">사용한 템플릿</span>
                                <span class="template-card__meta">${post.templateInfo.title}</span>
                            </div>
                            <button type="button"
                                    id="templateCopyBtn"
                                    class="template-card__copy-btn">
                                템플릿 복사
                            </button>
                        </div>
                        <textarea id="templateRaw" class="template-card__raw" hidden><c:out value="${post.templateInfo.rawContent}"/></textarea>
                        <div id="templatePreviewBody" class="template-card__body markdown-body"></div>
                    </div>
                </aside>
            </div>

            <section class="post-detail__comments">
                <h2 class="comments__title">댓글 3</h2>
                <div class="comment-form">
                    <textarea class="comment-form__textarea"
                              rows="3"
                              placeholder="댓글을 입력하세요. (마크다운 미지원)"></textarea>
                    <div class="comment-form__actions">
                        <span class="comment-form__hint">욕설, 비방 등은 제재될 수 있습니다.</span>
                        <button type="button" class="comment-form__submit">댓글 등록</button>
                    </div>
                </div>

                <ul class="comment-list">
                    <li class="comment">
                        <div class="comment__header">
                            <span class="comment__author">Jerry</span>
                            <span class="comment__dot">·</span>
                            <span class="comment__date">2025.11.09 10:12</span>
                        </div>
                        <div class="comment__body">첫 댓글입니다! 러닝 초보인데 거리/페이스 참고할게요 🙌</div>
                        <div class="comment__footer">
                            <button type="button"
                                    class="comment__reply-btn"
                                    data-reply-toggle>답글</button>

                            <div class="comment__actions">
                                <button type="button"
                                        class="comment__action-btn"
                                        data-comment-action="edit">수정</button>
                                <button type="button"
                                        class="comment__action-btn"
                                        data-comment-action="delete">삭제</button>
                                <button type="button"
                                        class="comment__action-btn comment__action-btn--danger"
                                        data-comment-action="report">신고</button>
                            </div>
                        </div>

                        <!-- 대댓글 작성폼 (초기엔 숨김) -->
                        <div class="reply-form is-hidden">
                            <textarea class="reply-form__textarea"
                                      rows="2"
                                      placeholder="답글을 입력하세요."></textarea>
                            <div class="reply-form__actions">
                                <button type="button"
                                        class="reply-form__cancel"
                                        data-reply-cancel>취소</button>
                                <button type="button"
                                        class="reply-form__submit">답글 등록</button>
                            </div>
                        </div>

                        <!-- 대댓글 리스트 -->
                        <ul class="reply-list">
                            <li class="reply">
                                <div class="reply__header">
                                    <span class="reply__author">인복</span>
                                    <span class="reply__dot">·</span>
                                    <span class="reply__date">2025.11.09 11:05</span>
                                </div>
                                <div class="reply__body">감사합니다! 천천히 거리 늘려가시면 금방 적응하실 거예요 💪</div>
                                <div class="reply__footer">
                                    <div class="reply__actions">
                                        <button type="button"
                                                class="reply__action-btn"
                                                data-comment-action="edit">수정</button>
                                        <button type="button"
                                                class="reply__action-btn"
                                                data-comment-action="delete">삭제</button>
                                        <button type="button"
                                                class="reply__action-btn reply__action-btn--danger"
                                                data-comment-action="report">신고</button>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>

                    <!-- 댓글 2 (대댓글 없는 경우) -->
                    <li class="comment">
                        <div class="comment__header">
                            <span class="comment__author">Runner</span>
                            <span class="comment__dot">·</span>
                            <span class="comment__date">2025.11.09 12:30</span>
                        </div>
                        <div class="comment__body">한내천 코스 좋죠! 저도 자주 뛰어요.</div>
                        <div class="comment__footer">
                            <button type="button"
                                    class="comment__reply-btn"
                                    data-reply-toggle>답글</button>

                            <div class="comment__actions">
                                <button type="button"
                                        class="comment__action-btn"
                                        data-comment-action="edit">수정</button>
                                <button type="button"
                                        class="comment__action-btn"
                                        data-comment-action="delete">삭제</button>
                                <button type="button"
                                        class="comment__action-btn comment__action-btn--danger"
                                        data-comment-action="report">신고</button>
                            </div>
                        </div>

                        <div class="reply-form is-hidden">
                            <textarea class="reply-form__textarea"
                                      rows="2"
                                      placeholder="답글을 입력하세요."></textarea>
                            <div class="reply-form__actions">
                                <button type="button"
                                        class="reply-form__cancel"
                                        data-reply-cancel>취소</button>
                                <button type="button"
                                        class="reply-form__submit">답글 등록</button>
                            </div>
                        </div>

                        <ul class="reply-list"><!-- 아직 대댓글 없음 --></ul>
                    </li>
                </ul>
            </section>

        </section>
    </main>
</div>

<!-- 푸터 -->
<jsp:include page="${lifelog.app.jsp}/layout/footer.jsp"/>

<script src="${lifelog.app.script.marked}"></script>
<script src="${lifelog.app.script.safeGuard}"></script>

<script src="${lifelog.app.js}/common/common.js"></script>

<script>
    // ===== 답글 토글 =====
    $(document).on('click', '[data-reply-toggle]', function () {
        const $comment = $(this).closest('.comment');
        $comment.find('.reply-form').toggleClass('is-hidden');
    });

    $(document).on('click', '[data-reply-cancel]', function () {
        const $comment = $(this).closest('.comment');
        $comment.find('.reply-form').addClass('is-hidden');
    });

    $('#btnDelete').on('click', function() {
        if (!confirm('정말 이 게시글을 삭제할까요?')) return;

        const postId = $(this).data('post-id');
        const params = {
            method: 'DELETE'
        }

        callApi('${lifelog.app.base}/api/post/${postId}', params)
            .then(res => {
                alert('삭제 되었습니다.');
                location.href = "/post";
            });
    });

    $('#templateCopyBtn').on('click', function () {
        var text = $('#templateRaw').text().trim();

        if (!text) {
            alert('복사할 템플릿 내용이 없습니다.');
            return;
        }

        if (navigator.clipboard && navigator.clipboard.writeText) {
            navigator.clipboard.writeText(text)
                .then(function () {
                    alert('템플릿 마크다운이 클립보드에 복사되었습니다.');
                })
                .catch(function () {
                    alert('복사에 실패했습니다. 브라우저 권한을 확인해주세요.');
                });
        } else {
            var temp = document.createElement('textarea');
            temp.value = text;
            document.body.appendChild(temp);
            temp.select();
            try {
                document.execCommand('copy');
                alert('템플릿 마크다운이 클립보드에 복사되었습니다.');
            } catch (e) {
                alert('복사에 실패했습니다.');
            } finally {
                document.body.removeChild(temp);
            }
        }
    });

    // ===== 댓글/대댓글: 수정·삭제·신고 버튼 클릭 핸들러 (현재는 알림만) =====
    $(document).on('click', '[data-comment-action]', function () {
        const action = $(this).data('comment-action'); // edit | delete | report
        const $commentBlock = $(this).closest('.comment, .reply');

        if (action === 'edit') {
            alert('수정 기능은 나중에 구현 예정입니다.');
            // TODO: 편집모드 토글, textarea로 바꾸기 등
        } else if (action === 'delete') {
            if (confirm('정말 삭제하시겠어요?')) {
                alert('삭제 기능은 나중에 API 연동 후 구현할 예정입니다.');
                // TODO: DELETE API 호출 + DOM에서 remove
            }
        } else if (action === 'report') {
            if (confirm('이 댓글을 신고하시겠어요?')) {
                alert('신고 기능은 나중에 API 연동 후 구현할 예정입니다.');
                // TODO: 신고 API 호출
            }
        }
    });

    function renderPreview() {
        var raw = $('#templateRaw').text().trim();
        if (!raw) return;

        var html = marked.parse(raw, { breaks: true, gfm: true });
        var safe = DOMPurify.sanitize(html, { USE_PROFILES: { html: true } });

        $('#templatePreviewBody').html(safe);
    }

    renderPreview();
</script>
</body>
</html>
