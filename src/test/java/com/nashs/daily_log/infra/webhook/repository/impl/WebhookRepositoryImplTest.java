package com.nashs.daily_log.infra.webhook.repository.impl;

import com.nashs.daily_log.ContainerTest;
import com.nashs.daily_log.domain.webhook.info.WebhookHistoryInfo;
import com.nashs.daily_log.infra.webhook.entity.WebhookHistory.WebhookPlatform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Collections;

import static com.nashs.daily_log.infra.webhook.entity.WebhookHistory.WebhookPlatform.DISCORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Sql(
        scripts = {
                "/test-data/user/user_insert.sql",
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(encoding = "UTF-8")
)
@Sql(
        scripts = {
                "/test-data/user/user_cleanup.sql",
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(encoding = "UTF-8")
)
class WebhookRepositoryImplTest extends ContainerTest {

    @Autowired
    private WebhookRepositoryImpl webhookRepository;

    @Test
    @DisplayName("웹훅 메세지 이력 저장")
    void saveWebhookHistory() {
        // given
        final String URL = "test url";
        final String CONTENT = "test content";
        final String RAW_CONTENT = "test raw content";
        final String USER_SUB = "user1";
        WebhookPlatform platform = DISCORD;
        WebhookHistoryInfo webhookHistoryInfo = WebhookHistoryInfo.builder()
                                                                  .url(URL)
                                                                  .content(CONTENT)
                                                                  .rawContent(RAW_CONTENT)
                                                                  .webhookPlatform(platform)
                                                                  .httpStatus(203)
                                                                  .isSuccess(true)
                                                                  .userSub(USER_SUB)
                                                                  .params(Collections.emptyMap())
                                                                  .build();

        // when
        WebhookHistoryInfo savedWebhookHistoryInfo = webhookRepository.saveWebhookHistory(webhookHistoryInfo);

        // then
        assertThat(savedWebhookHistoryInfo)
                .isNotNull()
                .satisfies(wh -> {
                    assertEquals(URL, wh.getUrl());
                    assertEquals(CONTENT, wh.getContent());
                    assertEquals(RAW_CONTENT, wh.getRawContent());
                    assertEquals(platform, wh.getWebhookPlatform());
                    assertTrue(wh.isSuccess());
                });
    }
}