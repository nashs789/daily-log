package com.nashs.daily_log.infra.template.repository.impl;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.template.repository.TemplateRepository;
import com.nashs.daily_log.infra.template.entity.Template;
import com.nashs.daily_log.infra.template.exception.TemplateInfraException;
import com.nashs.daily_log.infra.template.repository.TemplateJpaRepository;
import com.nashs.daily_log.infra.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nashs.daily_log.infra.template.exception.TemplateInfraException.TemplateInfraExceptionCode.NO_SUCH_TEMPLATE;


@Repository
@RequiredArgsConstructor
@Transactional
public class TemplateRepositoryImpl implements TemplateRepository {

    private final TemplateJpaRepository templateJpaRepository;

    @Override
    public TemplateInfo findTemplate(Long templateId) {
        return templateJpaRepository.findById(templateId)
                                    .orElseThrow(() -> new TemplateInfraException(NO_SUCH_TEMPLATE))
                                    .toInfo();
    }

    @Override
    public List<TemplateInfo> findAllTemplate(LifeLogUser lifeLogUser) {
        return templateJpaRepository.findAllByUser(User.fromLifeLogUser(lifeLogUser))
                                    .stream()
                                    .map(Template::toInfo)
                                    .toList();
    }

    @Override
    public TemplateInfo saveTemplate(TemplateInfo templateInfo) {
        return templateJpaRepository.save(Template.fromInfo(templateInfo))
                                    .toInfo();
    }

    @Override
    public void updateTemplate(TemplateInfo templateInfo) {
        templateJpaRepository.updateTemplate(
                templateInfo.getId(),
                templateInfo.getTitle(),
                templateInfo.getRawContent(),
                templateInfo.getParams(),
                templateInfo.getDiscord(),
                templateInfo.getSlack()
        );
    }

    @Override
    public void deleteTemplate(Long templateId) {
        Template template = templateJpaRepository.findById(templateId)
                                                 .orElseThrow(() -> new TemplateInfraException(NO_SUCH_TEMPLATE));

        templateJpaRepository.delete(template);
    }
}
