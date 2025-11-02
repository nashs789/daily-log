package com.nashs.daily_log.api.webhook.controller;

import com.nashs.daily_log.api.webhook.request.WebhookRequest;
import com.nashs.daily_log.application.webhook.WebhookFacade;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/webhook")
public class WebhookRestController {

    private final WebhookFacade webhookFacade;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessageToPlatform(
            @Valid @RequestBody WebhookRequest webhookRequest,
            LifeLogUser lifeLogUser
    ) {
        webhookFacade.sendMessageToPlatform(lifeLogUser, webhookRequest.templateId(), webhookRequest.webhookPlatform());

        log.info("webhook request: {}", webhookRequest);
        return ResponseEntity.noContent()
                             .build();
    }
}
