// ===== 이력 테이블 =====
(function(){
    const USE_DEMO_DATA = true;                     // ← 실제 API 쓰려면 false
    const API_URL = '/api/history';                 // ← 실제 API 경로

    // 데모 데이터
    const DEMO_ROWS = [
        { timestamp: Date.now()-3600e3*2, type:'INFO',  userName:'jerry', target:'POST#42',   summary:'게시글 보기', meta:{ip:'1.2.3.4'} },
        { timestamp: Date.now()-3600e3*1, type:'WARN',  userName:'mike',  target:'LOGIN',     summary:'로그인 1회 실패', meta:{ua:'Chrome'} },
        { timestamp: Date.now()-3600e3*3, type:'ERROR', userName:'anna',  target:'API /post', summary:'NullPointerException', meta:{traceId:'abc-123'} },
        { timestamp: Date.now()-600e3,    type:'INFO',  userName:'jerry', target:'POST#44',   summary:'댓글 작성', meta:{commentId:99} },
        { timestamp: Date.now()-200e3,    type:'AUDIT', userName:'root',  target:'ADMIN',     summary:'권한변경 user: mike → ROLE_EDITOR', meta:{by:'root'} },
        { timestamp: Date.now()-100e3,    type:'WARN',  userName:'mike',  target:'POST#10',   summary:'잘못된 접근 시도', meta:{path:'/admin'} },
        { timestamp: Date.now()-50e3,     type:'INFO',  userName:'jane',  target:'POST#51',   summary:'게시글 수정', meta:{fields:['title']} }
    ];

    function esc(s){ return typeof s === 'string' ? s.replace(/[&<>"']/g, m=>({ '&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;' }[m])) : s }

    // DataTables 초기화
    const table = $('#historyTable').DataTable({
        data: USE_DEMO_DATA ? DEMO_ROWS : [],
        ajax: USE_DEMO_DATA ? null : { url: API_URL, dataSrc: '' },
        columns: [
            { data:'timestamp', render:d => d ? new Date(d).toLocaleString() : '' , width: 160 },
            { data:'type', render: d => {
                    const t = (d||'').toUpperCase();
                    const cls = t==='ERROR' ? 'chip--error' : (t==='WARN' ? 'chip--warn' : 'chip--info');

                    return `<span class="chip ${cls}">${esc(t)}</span>`;
                }, width: 50
            },
            { data:'userName', defaultContent:'', width:50 },
            { data:'target', defaultContent:'' },
            { data:'summary', render:d=>`<span class="ellipsis" title="${esc(d||'')}">${esc(d||'')}</span>`, width: 320 },
            { data:'meta', render:d=> d ? `<span class="code" title="${esc(JSON.stringify(d))}">${esc(JSON.stringify(d))}</span>` : '' , width: 280 }
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

    // 유형 필터
    $('#typeSelect').on('change', function(){
        const val = this.value;
        // 타입 컬럼(1)만 부분검색
        table.column(1).search(val ? val : '').draw();
    });

    // 날짜 필터(클라이언트 사이드)
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

    $('#fromDate,#toDate').on('change', ()=> table.draw());

    // 새로고침
    $('#btnReload').on('click', function(){
        if (USE_DEMO_DATA){
            table.clear().rows.add(DEMO_ROWS).draw();
        } else {
            table.ajax.reload(null, false);
        }
    });
})();