package com.nashs.daily_log.infra.template.repository.impl;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.infra.template.entity.Template;
import com.nashs.daily_log.infra.template.exception.TemplateInfraException;
import com.nashs.daily_log.infra.template.repository.TemplateJpaRepository;
import com.nashs.daily_log.infra.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.nashs.daily_log.infra.template.exception.TemplateInfraException.TemplateInfraExceptionCode.NO_SUCH_TEMPLATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateRepositoryImplUnitTest {

    @Mock
    TemplateJpaRepository templateJpaRepository;

    @InjectMocks
    TemplateRepositoryImpl templateRepository;

    @Test
    @DisplayName("Unit: 저장된 템플릿 아이디로 조회")
    void findTemplate() {
        // given
        final Long TEMPLATE_ID = 1L;
        Template template = Template.builder()
                                    .id(TEMPLATE_ID)
                                    .user(User.builder()
                                              .build())
                                    .build();

        when(templateJpaRepository.findById(TEMPLATE_ID))
                .thenReturn(Optional.of(template));

        // when
        TemplateInfo templateInfo = templateRepository.findTemplate(TEMPLATE_ID);

        // then
        assertNotNull(templateInfo);
        assertEquals(TEMPLATE_ID, templateInfo.getId());
        verify(templateJpaRepository).findById(TEMPLATE_ID);
        verifyNoMoreInteractions(templateJpaRepository);
    }

    @Test
    @DisplayName("Unit: 존재하지 않는 템플릿 조회")
    void notExistTemplate() {
        // given
        final Long NOT_EXIST_TEMPLATE_ID = 999_999_999L;

        doThrow(new TemplateInfraException(NO_SUCH_TEMPLATE))
                .when(templateJpaRepository)
                .findById(NOT_EXIST_TEMPLATE_ID);

        // when & then
        assertThatThrownBy(() -> templateRepository.findTemplate(NOT_EXIST_TEMPLATE_ID))
                .isInstanceOf(TemplateInfraException.class);
        verify(templateJpaRepository).findById(NOT_EXIST_TEMPLATE_ID);
        verifyNoMoreInteractions(templateJpaRepository);
    }

    @Test
    @DisplayName("Unit: 유저가 소유한 모든 템플릿 조회")
    void findAllTemplate() {
        // given
        final String USER_SUB = "user1";
        User user = User.builder()
                        .sub(USER_SUB)
                        .build();
        LifeLogUser lifeLogUser = LifeLogUser.builder()
                                      .sub(USER_SUB)
                                      .build();
        List<Template> returnTemplate = List.of(
                Template.builder()
                        .user(user)
                        .build(),
                Template.builder()
                        .user(user)
                        .build(),
                Template.builder()
                        .user(user)
                        .build()
        );

        when(templateJpaRepository.findAllByUser(any(User.class)))
                .thenReturn(returnTemplate);

        // when
        List<TemplateInfo> allTemplate = templateRepository.findAllTemplate(lifeLogUser);

        // then
        assertThat(allTemplate)
                .isNotNull()
                .hasSize(returnTemplate.size());
        verifyNoMoreInteractions(templateJpaRepository);
    }

    @Test
    @DisplayName("Unit: 템플릿 저장")
    void saveTemplate() {
        // given
        final String USER_SUB = "user1";
        User user = User.builder()
                        .sub(USER_SUB)
                        .build();
        TemplateInfo templateInfo = TemplateInfo.builder()
                                                .userInfo(user.toInfo())
                                                .build();

        when(templateJpaRepository.save(any(Template.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Template.class));

        // when
        TemplateInfo savedTemplateInfo = templateRepository.saveTemplate(templateInfo);

        // then
        assertThat(savedTemplateInfo)
                .isNotNull()
                .extracting(TemplateInfo::getUserInfo)
                .extracting(UserInfo::getSub)
                .isEqualTo(USER_SUB);
        verify(templateJpaRepository).save(any(Template.class));
        verifyNoMoreInteractions(templateJpaRepository);
    }

    @Test
    @DisplayName("Unit: 템플릿 수정")
    void updateTemplate() {
        // given
        TemplateInfo templateInfo = TemplateInfo.builder()
                                                .userInfo(UserInfo.builder().build())
                                                .id(1L)
                                                .title("Before Title")
                                                .rawContent("RAW CONTENT")
                                                .params(Collections.emptyMap())
                                                .discord("DISCORD")
                                                .slack("SLACK")
                                                .build();

        // when
        templateRepository.updateTemplate(templateInfo);

        // then
        verify(templateJpaRepository).updateTemplate(
                templateInfo.getId(),
                templateInfo.getTitle(),
                templateInfo.getRawContent(),
                templateInfo.getParams(),
                templateInfo.getDiscord(),
                templateInfo.getSlack()
        );
        verifyNoMoreInteractions(templateJpaRepository);
    }

    @Test
    @DisplayName("Unit: 템플릿 정상 삭제")
    void deleteTemplate() {
        // given
        final Long TEMPLATE_ID = 1L;
        Template template = Template.builder()
                                    .id(TEMPLATE_ID)
                                    .build();

        when(templateJpaRepository.findById(TEMPLATE_ID))
                .thenReturn(Optional.of(template));

        // when
        templateRepository.deleteTemplate(TEMPLATE_ID);

        // then
        verify(templateJpaRepository).findById(TEMPLATE_ID);
        verify(templateJpaRepository).delete(template);
        verifyNoMoreInteractions(templateJpaRepository);
    }

    @Test
    @DisplayName("Unit: 존재하지 않는 템플릿 삭제 시도")
    void deleteNotOwnedTemplate() {
        // given
        final Long NOT_EXIST_TEMPLATE_ID = 999_999_999L;

        doThrow(new TemplateInfraException(NO_SUCH_TEMPLATE))
                .when(templateJpaRepository)
                .findById(NOT_EXIST_TEMPLATE_ID);

        // when & then
        assertThatThrownBy(() -> templateRepository.deleteTemplate(NOT_EXIST_TEMPLATE_ID))
                .isInstanceOf(TemplateInfraException.class);
        verify(templateJpaRepository).findById(NOT_EXIST_TEMPLATE_ID);
        verifyNoMoreInteractions(templateJpaRepository);
    }
}