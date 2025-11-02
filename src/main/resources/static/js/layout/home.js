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

    $paramList.find('input[data-token]')
              .each(function () {
                  map[$(this).data('token')] = $(this).val();
              });

    return map;
}

// ----- 렌더 파이프: 치환 → 마크다운 → HTML sanitize → 표시
let lastTokens = [];
function arraysEqual(a,b){
    if(a.length !== b.length) return false;

    for(let i = 0; i < a.length; i++)
        if(a[i] !== b[i]) return false;

    return true;
}

// 렌더 함수: skipRebuild 지원
function render(opts = {}) {
    const skipRebuild = !!opts.skipRebuild;
    const src = $tplText.val() || '';
    const tokens = extractTokens(src);

    if (!skipRebuild) {
        if (!arraysEqual(tokens, lastTokens)) {
            rebuildParamInputs(tokens);
            lastTokens = tokens;
        }
    }

    const vals = currentParamValues();
    let filled = src;
    (tokens || []).forEach(tok => {
        const val = (vals[tok] ?? '');
        filled = filled.split(tok).join(val);
    });

    const html = marked.parse(filled, { breaks: true, gfm: true });
    const safe = DOMPurify.sanitize(html, { USE_PROFILES: { html: true } });
    $preview.html(safe);
    return { filled, safe };
}

function setupParams() {
    const currentId = $tplLoad.val();

    if (!currentId) {
        $tplText.val('');
        render();
        $tplText.val(defaultTemplate);
        render();

        return;
    }

    const templateInfo = templateMap.get(currentId);

    $paramList.find('input[data-token]')
        .each(function () {
            const paramId = $(this).data('token');

            $(this).val(templateInfo.params[paramId]);
        });
}

function refreshSelect(templateList) {
    $tplLoad.find('option:not(:first)').remove();

    templateList.forEach(template => {
        templateMap.set(String(template.id), template)
        $tplLoad.append('<option value="' + template.id + '">' +template.title + '</option>');
    });
}