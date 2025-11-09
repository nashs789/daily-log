package com.nashs.daily_log.infra.template.entity;

import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.infra.user.entity.User;
import com.nashs.daily_log.infra.common.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.Objects;

@Getter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "template")
public class Template extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_template_user")
    )
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;

    @Column(name = "raw_content", columnDefinition = "text", nullable = false)
    private String rawContent;

    @Column
    private String discord;

    @Column
    private String slack;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "params", columnDefinition = "jsonb", nullable = false)
    private Map<String, String> params;

    public TemplateInfo toInfo() {
        return TemplateInfo.builder()
                           .id(id)
                           .userInfo(user.toInfo())
                           .title(title)
                           .content(content)
                           .rawContent(rawContent)
                           .discord(discord)
                           .slack(slack)
                           .params(params)
                           .build();
    }

    public static Template fromInfo(TemplateInfo info) {
        if (Objects.isNull(info)) {
            return null;
        }

        return Template.builder()
                       .id(info.getId())
                       .user(User.fromInfo(info.getUserInfo()))
                       .title(info.getTitle())
                       .content(info.getContent())
                       .rawContent(info.getRawContent())
                       .discord(info.getDiscord())
                       .slack(info.getSlack())
                       .params(info.getParams())
                       .build();
    }
}
