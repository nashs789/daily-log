package com.nashs.daily_log.domain.template.service;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.template.exception.TemplateDomainException;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nashs.daily_log.domain.template.exception.TemplateDomainException.TemplateDomainExceptionCode.NOT_TEMPLATE_OWNER;

@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    public void updateTemplate(LifeLogUser lifeLogUser, TemplateInfo templateInfo) throws TemplateDomainException {
        TemplateInfo findTemplate = templateRepository.findTemplate(templateInfo.getId());

        if (!lifeLogUser.sub().equals(findTemplate.getUserInfo().getSub())) {
            throw new TemplateDomainException(NOT_TEMPLATE_OWNER);
        }

        templateRepository.updateTemplate(templateInfo);
    }

    public List<TemplateInfo> findAllTemplate(LifeLogUser lifeLogUser) {
        return templateRepository.findAllTemplate(lifeLogUser);
    }

    public TemplateInfo saveTemplate(TemplateInfo templateInfo) {
        return templateRepository.saveTemplate(templateInfo);
    }
}
