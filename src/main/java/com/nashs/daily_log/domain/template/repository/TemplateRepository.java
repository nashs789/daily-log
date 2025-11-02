package com.nashs.daily_log.domain.template.repository;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository {
    TemplateInfo findTemplate(Long templateId);
    List<TemplateInfo> findAllTemplate(LifeLogUser lifeLogUser);
    TemplateInfo saveTemplate(TemplateInfo templateInfo);
    void updateTemplate(TemplateInfo templateInfo);
    void deleteTemplate(Long templateId);
}
