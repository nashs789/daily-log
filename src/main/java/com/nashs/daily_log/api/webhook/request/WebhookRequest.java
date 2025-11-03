package com.nashs.daily_log.api.webhook.request;

import com.nashs.daily_log.domain.webhook.info.WebhookInfo;
import com.nashs.daily_log.domain.webhook.info.WebhookInfo.WebhookPlatform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record WebhookRequest(
        @NotBlank(message = "내용을 입력 하세요.")
        @Size(max = 20_000, message = "내용은 20,000자 이하로 입력 하세요.")
        String rawContent,
        Map<String, String> params,
        WebhookPlatform webhookPlatform,
        String discord,
        String slack
) {
        public WebhookInfo toInfo() {
                return WebhookInfo.builder()
                                  .discord(discord)
                                  .slack(slack)
                                  .rawContent(rawContent)
                                  .params(params)
                                  .webhookPlatform(webhookPlatform)
                                  .build();
        }
}
