package com.nashs.daily_log.api.template.response;

import com.nashs.daily_log.domain.template.info.TemplateInfo;

import java.util.Map;

public record TemplateFindResponse(
        Long id,
        String title,
        String rawContent,
        String discord,
        String slack,
        Map<String, String>params
) {
    public static TemplateFindResponse fromInfo(TemplateInfo info) {
        return new TemplateFindResponse(
                info.getId(), info.getTitle(), info.getRawContent(),
                info.getDiscord(), info.getSlack(), info.getParams());
    }
}
