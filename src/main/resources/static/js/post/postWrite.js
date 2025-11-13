function init() {
    loadTemplateList();
}

function renderPreview() {
    const src  = $content.val() || '';
    const html = marked.parse(src, { breaks: true, gfm: true });
    const safe = DOMPurify.sanitize(html, { USE_PROFILES: { html: true } });

    $preview.html(safe);
}