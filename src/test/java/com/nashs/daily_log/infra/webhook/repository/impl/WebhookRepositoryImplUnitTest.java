package com.nashs.daily_log.infra.webhook.repository.impl;

import com.nashs.daily_log.domain.webhook.info.WebhookHistoryInfo;
import com.nashs.daily_log.infra.webhook.entity.WebhookHistory;
import com.nashs.daily_log.infra.webhook.repository.WebhookJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebhookRepositoryImplUnitTest {

    @Mock
    private WebhookJpaRepository webhookJpaRepository;

    @InjectMocks
    private WebhookRepositoryImpl webhookRepository;

    @Test
    @DisplayName("Unit: 웹훅 메세지 이력 저장")
    void saveWebhookHistory() {
        // given
        WebhookHistoryInfo webhookHistoryInfo = WebhookHistoryInfo.builder()
                                                                  .build();

        // when
        webhookRepository.saveWebhookHistory(webhookHistoryInfo);

        // then
        verify(webhookJpaRepository).save(any(WebhookHistory.class));
        verifyNoMoreInteractions(webhookJpaRepository);
    }
}