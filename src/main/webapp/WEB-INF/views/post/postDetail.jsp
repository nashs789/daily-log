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
<jsp:include page="${lifelog.app.jsp}/layout/top.jsp"/>

<div class="page">
    <aside class="sidebar-wrap">
        <jsp:include page="${lifelog.app.jsp}/layout/leftMenu.jsp"/>
    </aside>

    <main class="content">
        <section class="post-detail">
            <header class="post-detail__header">
                <div class="post-detail__title-row">
                    <h1 class="post-detail__title">${post.title}</h1>
                    <c:if test="${not empty lifeLogUser and lifeLogUser.name() eq post.userInfo.username}">
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
                <h2 class="comments__title">댓글 ${commentCount}</h2>
                <div class="comment-form">
                    <textarea id="comment_textarea"
                              class="comment-form__textarea"
                              rows="3"
                              placeholder="댓글을 입력하세요. (마크다운 미지원)"></textarea>
                    <div class="comment-form__actions">
                        <span class="comment-form__hint">욕설, 비방 등은 제재될 수 있습니다.</span>
                        <button type="button" id="comment_save_btn" class="comment-form__submit">댓글 등록</button>
                    </div>
                </div>

                <ul class="comment-list">
                    <c:forEach var="comment" items="${comment}">
                        <li class="comment" data-comment-id="${comment.id}">
                            <div class="comment__header">
                                <span class="comment__author">${comment.userInfo.username}</span>
                                <span class="comment__dot">·</span>
                                <span class="comment__date"><fmt:formatDate value="${comment.getCreatedTypeDate()}" pattern="yyyy.MM.dd HH:MM" /></span>
                            </div>
                            <div class="comment__body"><c:choose><c:when test="${comment.status == 'NORMAL'}">${comment.content}</c:when><c:otherwise><span class="comment__deleted">삭제된 댓글 입니다.</span></c:otherwise></c:choose>
                            </div>
                            <div class="comment__footer">
                                <button type="button" class="comment__reply-btn" data-reply-toggle>답글</button>
                                <div class="comment__actions">
                                    <%--<button type="button" class="comment__action-btn" data-comment-edit>수정</button>--%>
                                    <c:if test="${not empty lifeLogUser and lifeLogUser.name() eq comment.userInfo.username}">
                                        <button type="button" class="comment__action-btn" data-comment-delete>삭제</button>
                                    </c:if>
                                    <%--<button type="button" class="comment__action-btn comment__action-btn--danger" data-comment-report>신고</button>--%>
                                </div>
                            </div>
                            <div class="reply-form is-hidden">
                                <textarea class="reply-form__textarea" rows="2" placeholder="답글을 입력하세요."></textarea>
                                <div class="reply-form__actions">
                                    <button type="button" class="reply-form__cancel" data-reply-cancel>취소</button>
                                    <button type="button" class="reply-form__submit" data-reply-submit>답글 등록</button>
                                </div>
                            </div>
                        </li>
                        <c:forEach var="re" items="${reply[comment.id]}">
                            <ul class="reply-list">
                                <li class="reply">
                                    <div class="reply__header">
                                        <span class="reply__author">${re.userInfo.username}</span>
                                        <span class="reply__dot">·</span>
                                        <span class="reply__date"><fmt:formatDate value="${re.getCreatedTypeDate()}" pattern="yyyy.MM.dd HH:MM" /></span>
                                    </div>
                                    <div class="reply__body">${re.content}</div>
                                    <div class="reply__footer">
                                        <div class="reply__actions">
                                            <%--<button type="button" class="reply__action-btn" data-reply-edit>수정</button>--%>
                                            <button type="button" class="reply__action-btn" data-reply-delete="delete">삭제</button>
                                            <%--<button type="button" class="reply__action-btn reply__action-btn--danger" data-reply-report>신고</button>--%>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </c:forEach>
                    </c:forEach>
                </ul>
            </section>
        </section>
    </main>
</div>
<jsp:include page="${lifelog.app.jsp}/layout/footer.jsp"/>
</body>
</html>

<script src="${lifelog.app.script.marked}"></script>
<script src="${lifelog.app.script.safeGuard}"></script>

<script src="${lifelog.app.js}/common/common.js"></script>
<script src="${lifelog.app.js}/post/postDetail.js"></script>

<script>
    $(document).on('click', '[data-reply-toggle]', function () {
        const $comment = $(this).closest('.comment');

        $comment.find('.reply-form').toggleClass('is-hidden');
    });

    $(document).on('click', '[data-reply-cancel]', function () {
        const $comment = $(this).closest('.comment');

        $comment.find('.reply-form').addClass('is-hidden');
    });

    /**
     * 게시글 삭제 버튼 클릭
     */
    $('#btnDelete').on('click', function() {
        if (!confirm('정말 이 게시글을 삭제할까요?')) return;

        const params = {
            method: 'DELETE',
            dataType: 'text'
        }

        callApi('${lifelog.app.base}/api/post/${postId}', params)
            .then(res => {
                alert('삭제 되었습니다.');
                location.href = "/post";
            });
    });

    /**
     * 템플릿 복사 (ctrl + c)
     */
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

    function getCommentId(targetEl) {
        const $form = $(targetEl).closest('.comment__footer');
        const $scope = $form.closest('[data-comment-id]');

        return Number($scope.data('commentId'));
    }

    /**
     * 댓글 삭제 버튼 클릭
     */
    $(document).on('click', '[data-comment-delete]', function () {
        const commentId = getCommentId(this);
        const params = {
            method: 'DELETE',
            dataType: 'text',
        };

        callApi('${lifelog.app.base}/api/comment/' + commentId, params)
            .then(res => {
                alert('삭제 되었습니다.');
                location.reload();
            });
    });

    /**
     * 대댓글 등록 버튼
     */
    $(document).on('click', '[data-reply-submit]', function () {
        const { $textarea, parentId } = getCommentInputContext(this);
        const content = ($textarea.val() || '').trim();

        if (!parentId) {
            alert('부모 댓글 ID를 확인할 수 없습니다.');
            return;
        }

        if (!content) {
            alert('내용을 입력하세요.');
            $textarea.focus();
            return;
        }

        const params = {
            method: 'PUT',
            dataType: 'text',
            params: {
                parentId: parentId,
                postId: '${postId}',
                content: content
            }
        }

        callApi('${lifelog.app.base}/api/comment/reply', params)
            .then(res => {
                alert('저장 되었습니다.');
                location.reload();
            });
    });

    /**
     * 댓글 등록 버튼
     */
    $('#comment_save_btn').on('click', function() {
        const comment = $('#comment_textarea').val();
        const params = {
            method: 'PUT',
            dataType: 'text',
            params: {
                postId: '${postId}',
                content: comment
            }
        }

        callApi('${lifelog.app.base}/api/comment', params)
            .then(res => {
                alert('저장 되었습니다.');
                location.reload();
            });
    });

    renderPreview();
</script>
