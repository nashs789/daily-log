package com.nashs.daily_log.domain.webhook.service;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.webhook.exception.WebhookException;
import com.nashs.daily_log.domain.webhook.info.WebhookHistoryInfo;
import com.nashs.daily_log.domain.webhook.props.WebhookProps;
import com.nashs.daily_log.domain.webhook.repository.WebhookRepository;
import com.nashs.daily_log.infra.webhook.entity.WebhookHistory.WebhookPlatform;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.nashs.daily_log.domain.webhook.exception.WebhookException.WebhookExceptionCode.URL_NOT_VALID;
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
    private final WebhookRepository webhookRepository;

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

    private void sendErrorToSlack(final String title, final String msg) {
        Slack slack = props.getSlack();

        if (slack.isNotEnabled() || slack.hasNotWebhookUri()) return;

        checkSlackUrl(slack.getWebhook());
        sendMessage(
                slack.getWebhook(),
                Map.of("text", "*" + title + "*\n```" + msg + "```")
        );
    }

    private void sendErrorToDiscord(final String msg) {
        Discord discord = props.getDiscord();

        if (discord.isNotEnabled() || discord.hasNotWebhookUri()) return;

        checkDiscordUrl(discord.getWebhook());
        sendMessage(
                props.getDiscord().getWebhook(),
                Map.of("content", "```\n" + msg + "\n```")
        );
    }

    public void sendTemplateToPlatform(LifeLogUser lifeLogUser, WebhookHistoryInfo webhookHistoryInfo) {
        WebhookPlatform platform = webhookHistoryInfo.getWebhookPlatform();
        String toUrl = platform.isDiscord() ? webhookHistoryInfo.getDiscord() : webhookHistoryInfo.getSlack();
        String content = webhookHistoryInfo.getRawContent();
        Map<String, String> savedParams = webhookHistoryInfo.getParams();
        Map<Object, Object> platformParams = new HashMap<>();

        for (String key : savedParams.keySet()) {
            content = content.replace(key, savedParams.getOrDefault(key, ""));
        }

        if (platform.isDiscord()) {
            checkDiscordUrl(toUrl);
            platformParams.put("content", content);
        } else {
            checkSlackUrl(toUrl);
            platformParams.put("text", content);
        }

        try {
            webhookHistoryInfo.setupResponse(sendMessage(toUrl, platformParams));

            webhookHistoryInfo.setUrl(toUrl);
            webhookHistoryInfo.setContent(content);
            webhookHistoryInfo.setUserSub(lifeLogUser.sub());

            webhookRepository.saveWebhookHistory(webhookHistoryInfo);
        } catch (Exception ex) {
            // 비지니스 로직에 영향가지 않도록 예외 무시
//            webhookHistoryInfo.setErrorMessage(ex.getMessage());
//
//            webhookRepository.saveWebhookHistory(webhookHistoryInfo);
        }
    }

    private void checkDiscordUrl(String url) {
        if (!DISCORD.matcher(url).matches()) {
            throw new WebhookException(URL_NOT_VALID);
        }
    }

    private void checkSlackUrl(String url) {
        if (!SLACK.matcher(url).matches()) {
            throw new WebhookException(URL_NOT_VALID);
        }
    }

    private ResponseEntity<Void> sendMessage(String url, Map<Object, Object> params) {
        return client.post()
              .uri(url)
              .body(params)
              .retrieve()
              .toBodilessEntity();
    }
}
