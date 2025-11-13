package com.nashs.daily_log.domain.template.service;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.template.exception.TemplateDomainException;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.nashs.daily_log.domain.template.exception.TemplateDomainException.TemplateDomainExceptionCode.NOT_TEMPLATE_OWNER;

@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    public void updateTemplate(LifeLogUser lifeLogUser, TemplateInfo templateInfo) throws TemplateDomainException {
        checkUserOwnTemplate(lifeLogUser, templateInfo.getId());

        templateRepository.updateTemplate(templateInfo);
    }

    public List<TemplateInfo> findAllTemplate(LifeLogUser lifeLogUser) {
        return templateRepository.findAllTemplate(lifeLogUser.sub());
    }

    public TemplateInfo saveTemplate(TemplateInfo templateInfo) {
        return templateRepository.saveTemplate(templateInfo);
    }

    public void deleteTemplate(LifeLogUser lifeLogUser, Long templateId) {
        checkUserOwnTemplate(lifeLogUser, templateId);

        templateRepository.deleteTemplate(templateId);
    }

    public TemplateInfo checkUserOwnTemplate(LifeLogUser lifeLogUser, Long id) {
        if (Objects.isNull(id)) {
            return null;
        }

        TemplateInfo findTemplate = templateRepository.findTemplate(id);

        if (!lifeLogUser.sub().equals(findTemplate.getUserInfo().getSub())) {
            throw new TemplateDomainException(NOT_TEMPLATE_OWNER);
        }

        return findTemplate;
    }
}
