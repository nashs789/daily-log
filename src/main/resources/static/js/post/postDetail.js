function getCommentContext(targetEl) {
    const $form = $(targetEl).closest('.reply-form');
    // 부모 댓글 범위
    const $scope = $form.closest('[data-comment-id]');

    return {
        $form,
        $textarea: $form.find('.reply-form__textarea'),
        parentId: Number($scope.data('commentId')),
    };
}

function renderPreview() {
    var raw = $('#templateRaw').text().trim();
    if (!raw) return;

    var html = marked.parse(raw, { breaks: true, gfm: true });
    var safe = DOMPurify.sanitize(html, { USE_PROFILES: { html: true } });

    $('#templatePreviewBody').html(safe);
}