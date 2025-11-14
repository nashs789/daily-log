<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
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
                <c:if test="${lifeLogUser}">
                    <button id="tplSave" class="mdtpl__btn">저장</button>
                    <button id="tplDelete" class="mdtpl__btn mdtpl__btn--danger">삭제</button>
                </c:if>
            </div>

            <div class="mdtpl__grid">
                <div class="mdtpl__left">
                    <label class="mdtpl__label">템플릿 (마크다운)</label>
                    <textarea id="tplText" class="mdtpl__text" rows="12"></textarea>
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
                <div class="webhook-panel">
                    <div class="webhook-row">
                        <label for="slackWebhook">Slack Webhook</label>
                        <input id="slackWebhook" class="webhook-input" placeholder="https://hooks.slack.com/services/XXXXX/XXXXX/XXXXX" />
                        <button id="sendSlack" class="mdtpl__btn">슬랙 전송</button>
                    </div>

                    <div class="webhook-row">
                        <label for="discordWebhook">Discord Webhook</label>
                        <input id="discordWebhook" class="webhook-input" placeholder="https://discord.com/api/webhooks/XXXXX/XXXXX" />
                        <button id="sendDiscord" class="mdtpl__btn">디스코드 전송</button>
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

<script>
    const $tplText     = $('#tplText');
    const $paramList   = $('#paramList');
    const $preview     = $('#preview');
    const $tplName     = $('#tplName');
    const $tplSave     = $('#tplSave');
    const $tplLoad     = $('#tplLoad');
    const $copyRendered= $('#copyRendered');
    const defaultTemplate = `# 오늘 할일
- $1
- $2
- $3

> 메모: $?
`;
    $(document).ready(function() {
        init();
    });

    let composing = false;

    $paramList.on('compositionstart', 'input[data-token]', () => {
        composing = true;
    });

    $paramList.on('compositionend', 'input[data-token]', () => {
        composing = false; render({ skipRebuild: true });
    });

    $tplText.on('input', () => render());

    $paramList.on('input', 'input[data-token]', function () {
        if (composing) return;

        render({ skipRebuild: true });
    });

    $copyRendered.on('click', function () {
        const { filled } = render();

        navigator.clipboard.writeText(filled).then(() => {
            $copyRendered.text('복사됨!');
            setTimeout(() => $copyRendered.text('렌더된 텍스트 복사'), 1200);
        });
    });

    $tplLoad.on('change', function(){
        const id = $(this).val();
        const template = templateMap.get(id);

        $tplName.val(id ? template.title : '');
        $tplText.val(id ? template.rawContent : defaultTemplate);
        $('#discordWebhook').val(id ? template.discord : '');
        $('#slackWebhook').val(id ? template.slack : '');
        $tplSave.text(id ? '수정' : '저장');

        render();
        setupParams();
        render();
    });

    $tplSave.on('click', function(){
        const name = ($tplName.val() || '').trim();
        const user = '${lifeLogUser}';
        const id = $tplLoad.val();

        if (!user) return;

        if (!name) {
            alert('템플릿 이름을 입력하세요.');
            return;
        }

        const content = getContent();
        const { discord, slack } = getPlatformUrl();

        if ((discord && !isDiscordUrl(discord)) || (slack && !isSlackUrl(slack))) {
            alert('유효한 Webhook URL이 아닙니다.');
            return;
        }

        const opts = {
            method: id ? 'PATCH' : 'PUT',
            params: {
                id: id,
                title: name,
                content: JSON.stringify(content),
                rawContent: content,
                params: currentParamValues(),
                discord: discord,
                slack: slack
            }
        };

        callApi("${lifelog.app.base}/api/template", opts)
            .then(res => {
                alert('처리 되었습니다.');
                location.reload();
            });
    });

    $('#tplDelete').on('click', function(){
        const id = $tplLoad.val();

        if (!id) {
            alert('삭제할 템플릿을 선택하세요.');
            return;
        }

        if (!confirm('정말 삭제할까요?')) return;

        callApi(`${lifelog.app.base}/api/template/` + id, { method: 'DELETE' })
            .then(() => {
                alert('처리 되었습니다.');
                location.reload();
            });
    });

    $('#sendSlack').on('click', async function(){
        const url = $('#slackWebhook').val().trim();

        if (!isSlackUrl(url)) {
            alert('유효한 Slack Webhook URL이 아닙니다.'); return;
        }

        try {
            await sendTo('SLACK');
            alert('슬랙 전송 완료');
        } catch (e) {}
    });

    $('#sendDiscord').on('click', async function(){
        const url = $('#discordWebhook').val().trim();

        if (!isDiscordUrl(url)) {
            alert('유효한 Discord Webhook URL이 아닙니다.');
            return;
        }

        try {
            await sendTo('DISCORD');
            alert('디스코드 전송 완료');
        } catch (e) {}
    });

    function init() {
        const user = '${lifeLogUser}';

        $tplText.val(defaultTemplate);

        if (user) {
            loadTemplateList();
        }

        render();
        setupParams();
        setupWebhook();
    }

    async function sendTo(platform){
        const content = getContent();
        const { discord, slack } = getPlatformUrl();
        const opts = {
            params: {
                discord: discord,
                slack: slack,
                rawContent: content,
                params: currentParamValues(),
                webhookPlatform: platform
            }
        }

        return callApi(`${lifelog.app.base}/api/webhook/send`, opts);
    }

    async function loadTemplateList() {
        await callApi("${lifelog.app.base}/api/template", {method: 'GET'})
            .then(list => {
                refreshSelect(list);
            });
    }
</script>