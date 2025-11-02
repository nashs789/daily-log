package com.nashs.daily_log.api.webhook.request;

import com.nashs.daily_log.domain.webhook.enums.WebhookPlatform;

public record WebhookRequest(
        Long templateId,

        WebhookPlatform webhookPlatform
) {
}
