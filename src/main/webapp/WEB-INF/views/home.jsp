<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>DailyLog</title>
    <link rel="stylesheet" href="${lifelog.app.css}/layout/home.css"/>
</head>
<body>
<!-- 상단바 -->
<jsp:include page="${lifelog.app.jsp}/layout/top.jsp"/>

<div class="page">
    <!-- 좌측 메뉴 -->
    <aside class="sidebar-wrap">
        <jsp:include page="${lifelog.app.jsp}/layout/leftMenu.jsp"/>
    </aside>

    <!-- 메인 컨텐츠 -->
    <main class="content">
        <link rel="stylesheet" href="${lifelog.app.css}/layout/markdown.css"/>

        <div id="md-template" class="mdtpl">
            <div class="mdtpl__toolbar">
                <input id="tplName" class="mdtpl__name" placeholder="템플릿 이름 (예: 오늘 할일)" />
                <div class="mdtpl__spacer"></div>
                <select id="tplLoad" class="mdtpl__btn">
                    <option value="">저장된 템플릿 불러오기…</option>
                </select>
                <button id="tplSave"   class="mdtpl__btn">저장</button>
            </div>

            <div class="mdtpl__grid">
                <!-- 좌측: 템플릿 & 파라미터 -->
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
                        <div id="paramList" class="mdtpl__paramList"><!-- 자동 생성 --></div>
                    </div>
                </div>

                <!-- 우측: 미리보기 -->
                <div class="mdtpl__right">
                    <div class="mdtpl__previewHead">
                        <span>미리보기</span>
                        <button id="copyRendered" class="mdtpl__btn">렌더된 텍스트 복사</button>
                    </div>
                    <div id="preview" class="mdtpl__preview markdown-body"><!-- 렌더 출력 --></div>
                </div>
            </div>
        </div>

    </main>
</div>

<!-- 푸터 -->
<jsp:include page="${lifelog.app.jsp}/layout/footer.jsp"/>
</body>
</html>

<!-- 마크다운 파서 & XSS 세이프가드 -->
<script src="${lifelog.app.script.marked}"></script>
<script src="${lifelog.app.script.safeGuard}"></script>

<script>
    (function ($) {
        const $tplText     = $('#tplText');
        const $paramList   = $('#paramList');
        const $preview     = $('#preview');
        const $tplName     = $('#tplName');
        const $tplSave     = $('#tplSave');
        const $tplLoad     = $('#tplLoad');
        const $copyRendered= $('#copyRendered');

        // 기본 템플릿
        const defaultTpl = `# 오늘 할일
- $1
- $2
- $3

> 메모: $?
`;

        // ----- 유틸: 템플릿에서 파라미터 토큰 추출 (중복 제거, 등장 순 유지)
        function extractTokens(text) {
            const m = text.match(/\$[a-zA-Z0-9?]+/g) || [];
            const seen = new Set();
            return m.filter(t => (seen.has(t) ? false : (seen.add(t), true)));
        }

        // ----- UI: 파라미터 입력 필드 생성/동기화
        function rebuildParamInputs(tokens) {
            const focusedToken = $paramList.find('input:focus').data('token');
            const values = currentParamValues();
            $paramList.empty();
            tokens.forEach(tok => {
                const id = 'param_' + tok.replace('$','S');
                const $row = $(`
                  <div class="mdtpl__paramRow">
                    <label for="${id}" class="mdtpl__paramLabel">${tok}</label>
                    <input id="${id}" class="mdtpl__paramInput" type="text" data-token="${tok}" placeholder="${tok} 값을 입력" />
                  </div>
                `);

                if (values[tok] != null) {
                    $row.find('input').val(values[tok]);
                }

                $paramList.append($row);
            });

            if (focusedToken) {
                const $f = $paramList.find('input[data-token="'+focusedToken+'"]');
                if ($f.length) $f.focus();
            }
        }

        function currentParamValues() {
            const map = {};
            $paramList.find('input[data-token]').each(function () {
                map[$(this).data('token')] = $(this).val();
            });
            return map;
        }

        // ----- 렌더 파이프: 치환 → 마크다운 → HTML sanitize → 표시
        function render() {
            const src = $tplText.val() || '';
            const tokens = extractTokens(src);
            rebuildParamInputs(tokens);

            const vals = currentParamValues();
            let filled = src;
            tokens.forEach(tok => {
                const val = (vals[tok] ?? '');
                // 모든 등장 위치를 치환
                filled = filled.split(tok).join(val);
            });

            // markdown to HTML
            const html = marked.parse(filled, { breaks: true, gfm: true });
            // sanitize
            const safe = DOMPurify.sanitize(html, { USE_PROFILES: { html: true } });
            $preview.html(safe);
            return { filled, safe };
        }

        // 이벤트
        $tplText.on('input', render);
        $paramList.on('input', 'input[data-token]', render);
        $copyRendered.on('click', function () {
            const { filled } = render();
            navigator.clipboard.writeText(filled).then(() => {
                $copyRendered.text('복사됨!'); setTimeout(() => $copyRendered.text('렌더된 텍스트 복사'), 1200);
            });
        });

        // ----- 로컬 저장/불러오기 (데모용: localStorage)
        const LS_KEY = 'dailylog.templates';

        function loadAll() {
            try {
                return JSON.parse(localStorage.getItem(LS_KEY) || '{}');
            } catch(e) {
                return {};
            }
        }

        function saveAll(obj) {
            localStorage.setItem(LS_KEY, JSON.stringify(obj));
        }

        function refreshSelect() {
            const all = loadAll();
            $tplLoad.find('option:not(:first)').remove();
            Object.keys(all).forEach(name => {
                $tplLoad.append(`<option value="${name}">${name}</option>`);
            });
        }

        $tplSave.on('click', function(){
            const name = ($tplName.val() || '').trim();

            if (!name) {
                alert('템플릿 이름을 입력하세요.');
                return;
            }

            const all = loadAll();
            all[name] = { name, text: $tplText.val() || '' };
            saveAll(all);
            refreshSelect();
            alert('저장 되었습니다.');
        });

        $tplLoad.on('change', function(){
            const name = $(this).val();

            if (!name) return;

            const all = loadAll();

            if (all[name]) {
                $tplName.val(all[name].name);
                $tplText.val(all[name].text);
                render();
            }
        });

        // 초기화
        (function init(){
            refreshSelect();
            if (!$tplText.val()) $tplText.val(defaultTpl);
            render();
        })();

    })(window.jQuery);

    $.ajax({
        url: '${lifelog.app.base}/api/template',
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({ msg: 'hello' }),
        success: function (res) { console.log('PING OK:', res); },
        error: function (xhr) { console.error('PING ERR:', xhr.responseText); }
    });
</script>