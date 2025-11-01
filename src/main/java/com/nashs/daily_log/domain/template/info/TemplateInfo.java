package com.nashs.daily_log.domain.template.info;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.user.info.UserInfo;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class TemplateInfo {
    private Long id;
    private UserInfo userInfo;
    private String title;
    private String content;
    private String rawContent;
    private String discord;
    private String slack;
    private Map<String, String> params;

    public TemplateInfo setupUser(LifeLogUser lifeLogUser) {
        userInfo = lifeLogUser.toUserInfo();

        return this;
    }
}
