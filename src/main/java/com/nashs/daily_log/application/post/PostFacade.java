package com.nashs.daily_log.application.post;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.post.service.PostService;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.template.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
    private final TemplateService templateService;

    public PostInfo savePost(LifeLogUser lifeLogUser, PostInfo postInfo) {
        TemplateInfo templateInfo = postInfo.getTemplateInfo();
        Long templateId = Objects.nonNull(templateInfo) ? templateInfo.getId() : null;

        templateService.checkUserOwnTemplate(
                lifeLogUser, templateId
        );

        return postService.savePost(postInfo);
    }

    public void updatePost(LifeLogUser lifeLogUser, PostInfo postInfo) {
        TemplateInfo templateInfo = postInfo.getTemplateInfo();
        Long templateId = Objects.nonNull(templateInfo) ? templateInfo.getId() : null;

        templateService.checkUserOwnTemplate(
                lifeLogUser, templateId
        );

        postService.updatePostById(lifeLogUser, postInfo);
    }
}
