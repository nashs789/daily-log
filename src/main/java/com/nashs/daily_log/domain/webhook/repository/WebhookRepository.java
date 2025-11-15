package com.nashs.daily_log.domain.webhook.repository;

import com.nashs.daily_log.domain.webhook.info.WebhookHistoryInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface WebhookRepository {
    void saveWebhookHistory(WebhookHistoryInfo webhookHistoryInfo);
}
