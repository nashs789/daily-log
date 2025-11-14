(function() {
    function callApi(url, opts) {
        opts = opts || {};
        const method = (opts.method || 'POST').toUpperCase();
        const contentType = opts?.contentType ?? 'application/json';
        const params      = opts?.params ?? {};
        // 응답 받을 때 json 으로 파싱할 필요 없으면 text 로
        const dataType    = opts?.dataType ?? 'json';

        const requiredQueryString = /^(GET|HEAD|DELETE)$/i.test(method);
        const requiredJson = /^application\/json/i.test(contentType);
        const ajaxOptions = {
            url,
            type: method,
            dataType,
        }

        if (requiredQueryString) {
            ajaxOptions.data = params;
        } else if (requiredJson) {
            ajaxOptions.data = (typeof params === 'string') ? params : JSON.stringify(params);
            ajaxOptions.contentType = contentType;
            ajaxOptions.processData = false;
        } else {
            ajaxOptions.data = params;
            ajaxOptions.contentType = contentType;
        }

        return $.ajax(ajaxOptions)
                .fail(function (xhr) {
                    if (xhr.responseText) {
                        const res = JSON.parse(xhr.responseText);

                        alert(res.msg);
                    } else {
                        alert('요청에 실패하였습니다.');
                    }
                });
    }

    window.callApi = callApi;
})();