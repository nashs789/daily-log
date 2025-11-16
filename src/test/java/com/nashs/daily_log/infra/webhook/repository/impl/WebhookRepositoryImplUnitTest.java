package com.nashs.daily_log.infra.webhook.repository.impl;

import com.nashs.daily_log.domain.webhook.info.WebhookHistoryInfo;
import com.nashs.daily_log.infra.user.entity.User;
import com.nashs.daily_log.infra.webhook.entity.WebhookHistory;
import com.nashs.daily_log.infra.webhook.repository.WebhookJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebhookRepositoryImplUnitTest {

    @Mock
    private WebhookJpaRepository webhookJpaRepository;

    @InjectMocks
    private WebhookRepositoryImpl webhookRepository;

    @Test
    @DisplayName("Unit: 웹훅 이력 조회")
    void findWebhookHistories() {
        // given
        final String USER_SUB = "user1";
        User user = User.ref(USER_SUB);
        List<WebhookHistory> returnList = List.of(
                WebhookHistory.builder().user(user).build(),
                WebhookHistory.builder().user(user).build(),
                WebhookHistory.builder().user(user).build()
        );

        when(webhookJpaRepository.findAllByUserSub(anyString()))
                .thenReturn(returnList);

        // when
        List<WebhookHistoryInfo> webhookHistories = webhookRepository.findWebhookHistories(USER_SUB);

        // then
        assertThat(webhookHistories)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
        verify(webhookJpaRepository).findAllByUserSub(anyString());
        verifyNoMoreInteractions(webhookJpaRepository);
    }

    @Test
    @DisplayName("Unit: 웹훅 메세지 이력 저장")
    void saveWebhookHistory() {
        // given
        WebhookHistoryInfo webhookHistoryInfo = WebhookHistoryInfo.builder()
                                                                  .userSub("user1")
                                                                  .build();

        when(webhookJpaRepository.save(any(WebhookHistory.class)))
                .thenReturn(WebhookHistory.fromInfo(webhookHistoryInfo));

        // when
        WebhookHistoryInfo savedWebhookHistory = webhookRepository.saveWebhookHistory(webhookHistoryInfo);

        // then
        assertThat(savedWebhookHistory)
                .isNotNull();
        verify(webhookJpaRepository).save(any(WebhookHistory.class));
        verifyNoMoreInteractions(webhookJpaRepository);
    }
}