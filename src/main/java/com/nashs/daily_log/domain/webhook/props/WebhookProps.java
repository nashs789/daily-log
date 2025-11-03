package com.nashs.daily_log.domain.webhook.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Objects;

@Getter
@Component
@ConfigurationProperties(prefix = "notify")
public class WebhookProps {
    @Setter
    private boolean enabled  = true;
    private final Slack slack = new Slack();
    private final Discord discord = new Discord();

    public boolean isNotEnabled() {
        return !enabled;
    }

    @Getter
    @Setter
    public static class Messenger {
        private boolean enabled;
        private String webhook;

        public boolean isNotEnabled() {
            return !enabled;
        }

        public boolean hasNotWebhookUri() {
            return Objects.isNull(webhook);
        }
    }

    public static class Slack extends Messenger{}
    public static class Discord extends Messenger{}
}
