package com.nashs.daily_log.domain.post.info;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.common.utils.DateUtils;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.infra.post.entity.Post.PostStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Data
@Builder
public class PostInfo {
    private Long id;
    private UserInfo userInfo;
    private TemplateInfo templateInfo;
    private String title;
    private String content;
    private PostStatus status;
    private LocalDateTime created;
    private LocalDateTime modified;

    public TemplateInfo getTemplateInfo() {
        return Objects.nonNull(templateInfo) ? templateInfo : null;
    }

    public Date getCreatedTypeDate() {
        return DateUtils.LocalDateTimeToDate(created);
    }

    public PostInfo setupUser(LifeLogUser lifeLogUser) {
        userInfo = lifeLogUser.toUserInfo();

        if (Objects.nonNull(templateInfo)) {
            templateInfo.setUserInfo(userInfo);
        }

        return this;
    }
}
