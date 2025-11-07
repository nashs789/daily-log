package com.nashs.daily_log.domain.post.info;

import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.infra.post.entity.Post.PostStatus;
import lombok.Builder;
import lombok.Data;

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

    public TemplateInfo getTemplateInfo() {
        return Objects.nonNull(templateInfo) ? templateInfo : null;
    }
}
