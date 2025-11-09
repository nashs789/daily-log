package com.nashs.daily_log.api.post.request;

import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public record PostRequest(
        Long postId,

        @NotBlank(message = "제목을 입력 하세요.")
        @Size(max = 30, message = "제목은 30자 이하로 입력 하세요.")
        String title,

        @NotBlank(message = "내용을 입력 하세요.")
        @Size(max = 20_000, message = "내용은 20,000자 이하로 입력 하세요.")
        String content,

        Long templateId
) {
        public PostInfo toPostInfo() {
            return PostInfo.builder()
                           .id(postId)
                           .title(title)
                           .content(content)
                           .templateInfo(Objects.nonNull(templateId)
                                        ? TemplateInfo.builder().id(templateId).build()
                                        : null)
                           .build();
        }


}
