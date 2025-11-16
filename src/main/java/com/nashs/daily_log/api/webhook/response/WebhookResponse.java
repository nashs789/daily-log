package com.nashs.daily_log.api.webhook.response;

import com.nashs.daily_log.domain.common.utils.DateUtils;
import com.nashs.daily_log.domain.webhook.info.WebhookHistoryInfo;
import com.nashs.daily_log.infra.webhook.entity.WebhookHistory.WebhookPlatform;

import java.util.Date;
import java.util.Map;

public record WebhookResponse(
        WebhookPlatform platform,
        String url,
        String rawContent,
        Map<String, String> params,
        int httpStatus,
        boolean isSuccess,
        String errorMessage,
        Date created
) {

    public static WebhookResponse fromInfo(WebhookHistoryInfo info) {
        return new WebhookResponse(
            info.getWebhookPlatform(),
            info.getUrl(),
            info.getRawContent(),
            info.getParams(),
            info.getHttpStatus(),
            info.isSuccess(),
            info.getErrorMessage(),
            DateUtils.LocalDateTimeToDate(info.getCreated())
        );
    }
}
