package com.nashs.daily_log.api.template.response;

import com.nashs.daily_log.domain.template.info.TemplateInfo;

public record TemplateResponse(
        Long id,
        String title,
        String rawContent
) {
    public static TemplateResponse fromInfo(TemplateInfo info) {
        return new TemplateResponse(info.getId(), info.getTitle(), info.getRawContent());
    }
}
