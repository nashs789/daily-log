<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>글쓰기 - LifeLog</title>
  <link rel="stylesheet" href="${lifelog.app.css}/layout/home.css"/>
  <link rel="stylesheet" href="${lifelog.app.css}/post/postWrite.css"/>
</head>

<body>
<jsp:include page="${lifelog.app.jsp}/layout/top.jsp"/>

<div class="page">
  <aside class="sidebar-wrap">
    <jsp:include page="${lifelog.app.jsp}/layout/leftMenu.jsp"/>
  </aside>

  <main class="content">
    <div class="post-write">
      <div class="post-write__header">
        <div class="post-write__title-wrap">
          <label for="postTitle" class="post-write__label">제목</label>
          <input id="postTitle"
                 type="text"
                 class="post-write__title-input"
                 placeholder="제목을 입력하세요"/>
        </div>

        <div class="post-write__tpl-wrap">
          <label for="tplSelect" class="post-write__label">템플릿</label>
          <div class="post-write__tpl-row">
            <select id="tplSelect" class="post-write__tpl-select">
              <option value="">템플릿 선택…</option>
            </select>
          </div>
        </div>

        <div class="post-write__actions">
          <button type="button" id="btnSubmitPost" class="btn btn--primary">게시글 등록</button>
        </div>
      </div>

      <div class="post-write__editor">
        <section class="editor-pane">
          <div class="editor-pane__toolbar">
            <span class="editor-pane__title">내용</span>
          </div>
          <textarea id="postContent"
                    class="editor-pane__textarea"
                    placeholder="내용을 마크다운 또는 일반 텍스트로 작성하세요."></textarea>
        </section>

        <section class="preview-pane" id="previewPane">
          <div class="preview-pane__header">
            <span class="preview-pane__title">미리보기</span>
          </div>
          <article id="postPreview" class="preview-pane__body markdown-body"></article>
        </section>
      </div>
    </div>
    <textarea id="templateContent" hidden="hidden"></textarea>
  </main>
</div>


<jsp:include page="${lifelog.app.jsp}/layout/footer.jsp"/>
</body>
</html>

<script src="${lifelog.app.script.marked}"></script>
<script src="${lifelog.app.script.safeGuard}"></script>

<script src="${lifelog.app.js}/layout/home.js"></script>
<script src="${lifelog.app.js}/post/postWrite.js"></script>
<script src="${lifelog.app.js}/common/common.js"></script>

<script>
    const $content     = $('#templateContent');
    const $preview     = $('#postPreview');
    const $tplLoad     = $('#tplSelect');
    const $savePost    = $('#btnSubmitPost');

    $(document).ready(function() {
        init();
    });

    $tplLoad.on('change', function(){
        const id = $(this).val();
        const template = templateMap.get(id);

        $content.val(id ? template.rawContent : '');

        renderPreview();
    });

    $savePost.on('click', function() {
        const params = {
            method: 'PUT',
            params: {
                title: $('#postTitle').val(),
                content: $('#postContent').val(),
                templateId: $tplLoad.val()
            }
        }

        callApi('${lifelog.app.base}/api/post', params)
                .then(res => {
                    alert('저장 되었습니다.');
                    location.href = "/post";
                });
    });

    async function loadTemplateList() {
        await callApi("${lifelog.app.base}/api/template", {method: 'GET'})
                .then(list => {
                  refreshSelect(list);
                });
    }
</script>
