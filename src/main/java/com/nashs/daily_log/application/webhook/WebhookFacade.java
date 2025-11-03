package com.nashs.daily_log.application.webhook;

import com.nashs.daily_log.domain.template.service.TemplateService;
import com.nashs.daily_log.domain.webhook.info.WebhookInfo;
import com.nashs.daily_log.domain.webhook.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookFacade {

    private final TemplateService templateService;
    private final WebhookService webhookService;

    public void sendMessageToPlatform(WebhookInfo webhookInfo) {
        webhookService.sendTemplateToPlatform(webhookInfo);
    }
}
