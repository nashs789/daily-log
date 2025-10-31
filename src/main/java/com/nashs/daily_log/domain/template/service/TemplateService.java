package com.nashs.daily_log.domain.template.service;

import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateInfo saveTemplate(TemplateInfo templateInfo) {
        return templateRepository.saveTemplate(templateInfo);
    }
}
