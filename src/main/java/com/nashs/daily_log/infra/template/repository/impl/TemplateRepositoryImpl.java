package com.nashs.daily_log.infra.template.repository.impl;

import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.template.repository.TemplateRepository;
import com.nashs.daily_log.infra.template.entity.Template;
import com.nashs.daily_log.infra.template.repository.TemplateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TemplateRepositoryImpl implements TemplateRepository {

    private final TemplateJpaRepository templateJpaRepository;

    @Override
    public TemplateInfo saveTemplate(TemplateInfo templateInfo) {
        return templateJpaRepository.save(Template.fromInfo(templateInfo))
                                    .toInfo();
    }
}
