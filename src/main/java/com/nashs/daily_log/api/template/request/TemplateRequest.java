package com.nashs.daily_log.api.template.request;

import com.nashs.daily_log.domain.template.info.TemplateInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record TemplateRequest(
        Long id,

        @NotBlank(message = "제목을 입력 하세요.")
        @Size(max = 30, message = "제목은 30자 이하로 입력 하세요.")
        String title,

        @NotBlank(message = "내용을 입력 하세요.")
        @Size(max = 20_000, message = "내용은 20,000자 이하로 입력 하세요.")
        String content,

        @NotBlank(message = "내용을 입력 하세요.")
        @Size(max = 20_000, message = "내용은 20,000자 이하로 입력 하세요.")
        String rawContent,

        String discord,

        String slack,

        Map<String, String> params
) {
        public TemplateInfo toInfo() {
                return TemplateInfo.builder()
                                   .id(id)
                                   .title(title)
                                   .content(content)
                                   .rawContent(rawContent)
                                   .params(params)
                                   .discord(discord)
                                   .slack(slack)
                                   .build();
        }
}
