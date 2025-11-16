package com.nashs.daily_log.domain.webhook.repository;

import com.nashs.daily_log.domain.webhook.info.WebhookHistoryInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebhookRepository {
    List<WebhookHistoryInfo> findWebhookHistories(String userSub);
    WebhookHistoryInfo saveWebhookHistory(WebhookHistoryInfo webhookHistoryInfo);
}
