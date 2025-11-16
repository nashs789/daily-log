<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>LifeLog · 이력 조회</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.6/css/dataTables.dataTables.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/3.0.2/css/buttons.dataTables.min.css">
    <link rel="stylesheet" href="${lifelog.app.css}/layout/home.css"/>
    <link rel="stylesheet" href="${lifelog.app.css}/webhook/history.css"/>
</head>
<body>

<jsp:include page="${lifelog.app.jsp}/layout/top.jsp"/>

<div class="layout">
    <aside class="sidebar-wrap">
        <jsp:include page="${lifelog.app.jsp}/layout/leftMenu.jsp"/>
    </aside>

    <!-- Content -->
    <main class="content">
        <div class="page-title" style="color: darkblue">
            <h1>이력 조회</h1>
            <div class="metric">표시 중: <strong id="totalCount" style="color: darkblue">0</strong>건</div>
        </div>

        <div class="card">
            <div class="toolbar">
                <input type="date" class="ctl" id="fromDate">
                <span style="color:#9ca3af">~</span>
                <input type="date" class="ctl" id="toDate">
                <select class="ctl" id="typeSelect">
                    <option value="">유형(전체)</option>
                    <option value="INFO">INFO</option>
                    <option value="WARN">WARN</option>
                    <option value="ERROR">ERROR</option>
                    <option value="AUDIT">AUDIT</option>
                </select>
            </div>

            <!-- Table -->
            <table id="historyTable" class="display nowrap stripe" style="width:100%">
                <thead>
                <tr>
                    <th>시간</th>
                    <th>유형</th>
                    <th>사용자</th>
                    <th>대상</th>
                    <th>내용</th>
                    <th>메타</th>
                </tr>
                </thead>
            </table>
        </div>
    </main>
</div>
<jsp:include page="${lifelog.app.jsp}/layout/footer.jsp"/>
</body>
</html>

<!-- JS -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.datatables.net/2.1.6/js/dataTables.min.js"></script>
<script src="https://cdn.datatables.net/buttons/3.0.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/3.0.2/js/buttons.html5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/3.0.2/js/buttons.print.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>

<script src="${lifelog.app.js}/webhook/history.js"></script>
