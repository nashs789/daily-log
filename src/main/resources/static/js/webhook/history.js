const API_URL = '/api/webhook/history';                 // ← 실제 API 경로

function esc(s) {
    return typeof s === 'string'
        ? s.replace(/[&<>"']/g, m => ({
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#39;'}[m]))
        : s;
}

const table = $('#historyTable').DataTable({
    ajax: {
        url: API_URL,
        dataSrc: ''
    },
    columns: [
        {
            data: 'created',
            render: d => d ? new Date(d).toLocaleString() : '',
            width: 180
        },
        {
            data: 'platform',
            render: d => {
                const t = (d || '').toUpperCase();
                return `<span class="chip chip--info">${esc(t)}</span>`;
            },
            width: 90
        },
        {
            data: 'isSuccess',
            render: d => {
                const ok = !!d;
                const cls = ok ? 'chip--ok' : 'chip--error';
                const label = ok ? '성공' : '실패';
                return `<span class="chip ${cls}">${label}</span>`;
            },
            width: 90
        },
        {
            data: 'httpStatus',
            render: d => d != null ? `<span class="code">${d}</span>` : '',
            width: 70
        },
        {
            data: 'url',
            render: d => {
                const text = d || '';
                return text
                    ? `<span class="ellipsis" title="${esc(text)}">${esc(text)}</span>`
                    : '';
            },
            width: 260
        },
        {
            data: 'rawContent',
            render: d => {
                const text = d || '';
                return text
                    ? `<span class="ellipsis" title="${esc(text)}">${esc(text)}</span>`
                    : '';
            },
            width: 320
        },
        {
            data: 'params',
            render: d => {
                if (!d) return '';
                const json = JSON.stringify(d);
                return `<span class="code" title="${esc(json)}">${esc(json)}</span>`;
            },
            width: 260
        }
    ],
    order: [[0,'desc']],
    pageLength: 20,
    lengthMenu: [10,20,50,100],
    responsive: true,
    scrollX: true,
    scrollY: 600,
    dom: 'Bfrtip',
    scrollCollapse: false,
    buttons: [],
    initComplete: function(){
        updateCount();
    }
});

function updateCount(){
    const info = table.page.info();

    document.getElementById('totalCount').textContent = info.recordsDisplay ?? info.recordsTotal ?? 0;
}

table.on('draw', updateCount);

$('#typeSelect').on('change', function(){
    const val = this.value;
    // 타입 컬럼(1)만 부분검색
    table.column(1).search(val ? val : '').draw();
});

$.fn.dataTable.ext.search.push(function(settings, data){
    const colDate = data[0]; // 렌더된 문자열
    const ts = new Date(colDate).getTime();
    if (!ts) return true;

    const fromVal = $('#fromDate').val();
    const toVal   = $('#toDate').val();

    const from = fromVal ? new Date(fromVal + 'T00:00:00').getTime() : null;
    const to   = toVal   ? new Date(toVal   + 'T23:59:59').getTime() : null;

    if (from && ts < from) return false;
    if (to   && ts > to)   return false;
    return true;
});

$('#fromDate, #toDate').on('change', ()=> table.draw());