package com.nashs.daily_log.api.post.response;

import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.user.info.UserInfo;

public record PostResponse(
        Long id,
        UserInfo userInfo,
        TemplateInfo templateInfo,
        String title,
        String content
) {
    public static PostResponse fromInfo(PostInfo postInfo) {
        return new PostResponse(
                postInfo.getId(),
                postInfo.getUserInfo(),
                postInfo.getTemplateInfo(),
                postInfo.getTitle(),
                postInfo.getContent()
        );
    }
}
