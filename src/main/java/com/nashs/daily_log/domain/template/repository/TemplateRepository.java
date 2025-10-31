package com.nashs.daily_log.domain.template.repository;

import com.nashs.daily_log.domain.template.info.TemplateInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository {
    TemplateInfo saveTemplate(TemplateInfo templateInfo);
}
