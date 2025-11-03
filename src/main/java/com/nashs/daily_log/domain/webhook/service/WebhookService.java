package com.nashs.daily_log.domain.webhook.service;

import com.nashs.daily_log.domain.webhook.info.WebhookInfo;
import com.nashs.daily_log.domain.webhook.info.WebhookInfo.WebhookPlatform;
import com.nashs.daily_log.domain.webhook.props.WebhookProps;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.nashs.daily_log.domain.webhook.props.WebhookProps.Discord;
import static com.nashs.daily_log.domain.webhook.props.WebhookProps.Slack;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final Pattern SLACK = Pattern.compile("^https://hooks\\.slack\\.com/services/.+");
    private final Pattern DISCORD = Pattern.compile("^https://(discord(?:app)?\\.com)/api/webhooks/.+");

    private final WebhookProps props;
    private final RestClient client;

    public void sendExceptionAsync(Exception ex, HttpServletRequest req) {
        if (props.isNotEnabled() || Objects.isNull(req)) {
            log.error("sendExceptionAsync = {} {}", ex, req);
            return;
        }

        try {
            String path = req.getRequestURI();
            String method = req.getMethod();
            LocalDateTime time = LocalDateTime.now();
            String topStack = Arrays.stream(ex.getStackTrace())
                                    .limit(3)
                                    .map(StackTraceElement::toString)
                                    .collect(Collectors.joining(System.lineSeparator()));
            String title = String.format("[Error]: %s", ex.getClass().getName());
            String msg = String.format("%s %s (%s) \nmsg: %s\nstack(top3):\n%s", method, path, time, ex.getMessage(), topStack);

            sendErrorToSlack(title, msg);
            sendErrorToDiscord(msg);
        } catch (Exception e) {
            log.error("sendExceptionAsync", e);
        }
    }

    public void sendErrorToSlack(final String title, final String msg) {
        Slack slack = props.getSlack();

        if (slack.isNotEnabled() || slack.hasNotWebhookUri()) return;

        client.post()
              .uri(slack.getWebhook())
              .body(Map.of(
                      "text", "*" + title + "*\n```" + msg + "```"
              ))
              .retrieve()
              .toBodilessEntity();
    }

    public void sendErrorToDiscord(final String msg) {
        Discord discord = props.getDiscord();

        if (discord.isNotEnabled() || discord.hasNotWebhookUri()) return;

        client.post()
              .uri(props.getDiscord().getWebhook())
              .body(Map.of("content", "```\n" + msg + "\n```"))
              .retrieve()
              .toBodilessEntity();
    }

    public void sendTemplateToPlatform(WebhookInfo webhookInfo) {
        WebhookPlatform platform = webhookInfo.getWebhookPlatform();
        String toUrl = platform.isDiscord() ? webhookInfo.getDiscord() : webhookInfo.getSlack();
        String content = webhookInfo.getRawContent();
        Map<String, String> savedParams = webhookInfo.getParams();
        Map<Object, Object> platformParams = new HashMap<>();

        for (String key : savedParams.keySet()) {
            content = content.replace(key, savedParams.getOrDefault(key, ""));
        }

        if (platform.isDiscord()) {
            platformParams.put("content", content);
        } else {
            platformParams.put("text", content);
        }

        client.post()
              .uri(toUrl)
              .body(platformParams)
              .retrieve()
              .toBodilessEntity();
    }
}
