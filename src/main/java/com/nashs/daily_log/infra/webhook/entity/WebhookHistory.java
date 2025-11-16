package com.nashs.daily_log.infra.webhook.entity;

import com.nashs.daily_log.domain.webhook.info.WebhookHistoryInfo;
import com.nashs.daily_log.infra.common.entity.Timestamp;
import com.nashs.daily_log.infra.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "webhook_history")
public class WebhookHistory extends Timestamp {

    @Getter
    @RequiredArgsConstructor
    public enum WebhookPlatform {
        DISCORD,
        SLACK;

        public boolean isDiscord() {
            return this == DISCORD;
        }

        public boolean isSlack() {
            return this == SLACK;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_webhook_history_user")
    )
    private User user;

    @Enumerated(EnumType.STRING)
    private WebhookPlatform platform;

    @Column(name = "url")
    private String url;

    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;

    @Column(name = "raw_content", columnDefinition = "text", nullable = false)
    private String rawContent;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "params", columnDefinition = "jsonb", nullable = false)
    private Map<String, String> params;

    @Column(name = "http_status")
    private int httpStatus;

    @Column(name = "is_success")
    private boolean isSuccess;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    public static WebhookHistory fromInfo(WebhookHistoryInfo info) {
        return WebhookHistory.builder()
                             .user(User.ref(info.getUserSub()))
                             .platform(info.getWebhookPlatform())
                             .url(info.getUrl())
                             .content(info.getContent())
                             .rawContent(info.getRawContent())
                             .params(info.getParams())
                             .httpStatus(info.getHttpStatus())
                             .isSuccess(info.isSuccess())
                             .errorMessage(info.getErrorMessage())
                             .build();
    }

    public WebhookHistoryInfo toInfo() {
        return WebhookHistoryInfo.builder()
                                 .userSub(user.getSub())
                                 .webhookPlatform(platform)
                                 .url(url)
                                 .content(content)
                                 .rawContent(rawContent)
                                 .params(params)
                                 .httpStatus(httpStatus)
                                 .isSuccess(isSuccess)
                                 .errorMessage(errorMessage)
                                 .created(getCreated())
                                 .build();
    }
}
