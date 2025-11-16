package com.nashs.daily_log.domain.webhook.info;

import com.nashs.daily_log.infra.webhook.entity.WebhookHistory.WebhookPlatform;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class WebhookHistoryInfo {

    private String userSub;
    private String discord;
    private String slack;
    private String content;
    private WebhookPlatform webhookPlatform;
    private String url;
    private String rawContent;
    private Map<String, String> params;
    private int httpStatus;
    private boolean isSuccess;
    private String errorMessage;
    private LocalDateTime created;

    public void setupResponse(ResponseEntity<Void> response) {
        httpStatus = response.getStatusCode().value();
        isSuccess = response.getStatusCode().is2xxSuccessful();
    }
}
