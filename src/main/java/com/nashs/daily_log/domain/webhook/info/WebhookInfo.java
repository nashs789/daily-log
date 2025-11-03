package com.nashs.daily_log.domain.webhook.info;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class WebhookInfo {

    private String discord;
    private String slack;
    private String rawContent;
    private Map<String, String> params;
    private WebhookPlatform webhookPlatform;

    public enum WebhookPlatform {
        DISCORD,
        SLACK;

        public boolean isDiscord() {
            return this == DISCORD;
        }

        public boolean isSlack() {
            return this == SLACK;
        }
    }
}
