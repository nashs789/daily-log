package com.nashs.daily_log.domain.notify.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.nashs.daily_log.domain.notify.service.NotifyProperties.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {
    private final NotifyProperties props;
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
                                    .limit(7)
                                    .map(StackTraceElement::toString)
                                    .collect(Collectors.joining(System.lineSeparator()));
            String title = String.format("[Error]: %s", ex.getClass().getName());
            String msg = String.format("%s %s (%s) \nmsg: %s\nstack(top7):\n%s", method, path, time, ex.getMessage(), topStack);

            sendSlack(title, msg);
            sendDiscord(msg);
        } catch (Exception e) {
            log.error("sendExceptionAsync", e);
        }
    }

    public void sendSlack(String title, String msg) {
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

    public void sendDiscord(String msg) {
        Discord discord = props.getDiscord();

        if (discord.isNotEnabled() || discord.hasNotWebhookUri()) return;

        client.post()
              .uri(props.getDiscord().getWebhook())
              .body(Map.of("content", "```\n" + msg + "\n```"))
              .retrieve()
              .toBodilessEntity();
    }
}
