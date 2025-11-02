package com.nashs.daily_log.domain.webhook.enums;

public enum WebhookPlatform {
    DISCORD,
    SLACK;

    public boolean isDiscord() {
        return this == WebhookPlatform.DISCORD;
    }

    public boolean isSlack() {
        return this == WebhookPlatform.SLACK;
    }
}
