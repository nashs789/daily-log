package com.nashs.daily_log.infra.webhook.repository.impl;

import com.nashs.daily_log.domain.webhook.info.WebhookHistoryInfo;
import com.nashs.daily_log.domain.webhook.repository.WebhookRepository;
import com.nashs.daily_log.infra.webhook.entity.WebhookHistory;
import com.nashs.daily_log.infra.webhook.repository.WebhookJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@RequiredArgsConstructor
public class WebhookRepositoryImpl implements WebhookRepository {

    private final WebhookJpaRepository webhookRepository;

    @Override
    public WebhookHistoryInfo saveWebhookHistory(WebhookHistoryInfo webhookHistoryInfo) {
        return webhookRepository.save(WebhookHistory.fromInfo(webhookHistoryInfo))
                                .toInfo();
    }
}
