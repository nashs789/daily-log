package com.nashs.daily_log.api.webhook.controller;

import com.nashs.daily_log.api.webhook.request.WebhookRequest;
import com.nashs.daily_log.api.webhook.response.WebhookResponse;
import com.nashs.daily_log.application.webhook.WebhookFacade;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.webhook.service.WebhookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/webhook")
public class WebhookRestController {

    private final WebhookService webhookService;
    private final WebhookFacade webhookFacade;

    @GetMapping("/history")
    public ResponseEntity<List<WebhookResponse>> test(
            LifeLogUser lifeLogUser
    ) {
        return ResponseEntity.ok()
                             .body(webhookService.findWebhookHistories(lifeLogUser.sub())
                                                 .stream()
                                                 .map(WebhookResponse::fromInfo)
                                                 .toList());
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessageToPlatform(
            @Valid @RequestBody WebhookRequest webhookRequest,
            LifeLogUser lifeLogUser
    ) {
        webhookFacade.sendMessageToPlatform(lifeLogUser, webhookRequest.toInfo());

        return ResponseEntity.noContent()
                             .build();
    }
}
