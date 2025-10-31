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
            const id = 'param_' + tok.replace('$', 'S');
            const $row = $(`
                  <div class="mdtpl__paramRow">
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
