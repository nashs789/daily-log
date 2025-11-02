(function() {
    function callApi(url, opts) {
        opts = opts || {};
        const method = (opts.method || 'POST').toUpperCase();
        const contentType = opts?.contentType ?? 'application/json';
        const params      = opts?.params ?? {};
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

        console.log(ajaxOptions)
        return $.ajax(ajaxOptions)
                .fail(function (xhr) {
                    console.error('API ERROR:', method, url, xhr);
                });
    }

    window.callApi = callApi;
})();