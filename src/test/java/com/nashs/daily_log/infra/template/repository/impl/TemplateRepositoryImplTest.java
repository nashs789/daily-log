package com.nashs.daily_log.infra.template.repository.impl;

import com.nashs.daily_log.ContainerTest;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.domain.user.repository.UserRepository;
import com.nashs.daily_log.infra.template.exception.TemplateInfraException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest
@ActiveProfiles("test")
@Sql(
        scripts = {
                "/test-data/user/user.sql",
                "/test-data/template/template.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(encoding = "UTF-8")
)
@Sql(
        scripts = {
                "/test-data/user/user_cleanup.sql",
                "/test-data/template/template_cleanup.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(encoding = "UTF-8")
)
class TemplateRepositoryImplTest extends ContainerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateRepositoryImpl templateRepository;

    @Test
    @DisplayName("저장된 템플릿 아이디로 조회")
    void findTemplate() {
        // given
        final Long TEMPLATE_ID = 1L;
        final String USER_SUB = "user1";

        // when
        TemplateInfo template = templateRepository.findTemplate(TEMPLATE_ID);

        // then
        assertThat(template)
                .isNotNull()
                .extracting(TemplateInfo::getId, t -> t.getUserInfo().getSub())
                .containsExactly(TEMPLATE_ID, USER_SUB);
    }

    @Test
    @DisplayName("존재하지 않는 템플릿 조회")
    void notExistTemplate() {
        // given
        final Long NOT_EXIST_TEMPLATE_ID = 999_999_999L;

        // when & then
        assertThatThrownBy(() -> templateRepository.findTemplate(NOT_EXIST_TEMPLATE_ID))
                .isInstanceOf(TemplateInfraException.class)
                .extracting("status")
                .isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("유저가 소유한 모든 템플릿 조회")
    void findAllTemplate() {
        // given
        final String USER_SUB = "user1";
        UserInfo userInfo = userRepository.findBySub(USER_SUB);

        // when
        List<TemplateInfo> allTemplate = templateRepository.findAllTemplate(userInfo.toLifeLogUser());

        // then
        assertNotNull(allTemplate);
        assertThat(allTemplate.stream()
                              .map(TemplateInfo::getUserInfo)
                              .map(UserInfo::getSub)
                              .distinct()
                              .count()).isEqualTo(1);
    }

    @Test
    @DisplayName("템플릿 저장")
    void saveTemplate() {
        // given
        final String USER_SUB = "user1";
        TemplateInfo templateInfo = TemplateInfo.builder()
                                                .userInfo(UserInfo.builder()
                                                                  .sub(USER_SUB)
                                                                  .build())
                                                .title("test title for not null condition")
                                                .content("test content for not null condition")
                                                .rawContent("test rawContent for not null condition")
                                                .params(Collections.emptyMap())
                                                .build();

        // when
        TemplateInfo savedTemplate = templateRepository.saveTemplate(templateInfo);

        // then
        assertAll(() -> {
            assertNotNull(savedTemplate);
            assertNotNull(savedTemplate.getId());
            assertEquals(templateInfo.getTitle(), savedTemplate.getTitle());
            assertEquals(USER_SUB, savedTemplate.getUserInfo().getSub());
        });
    }

    @Test
    @DisplayName("템플릿 수정")
    void updateTemplate() {
        // given
        final Long TEMPLATE_ID = 1L;
        final String UPDATED_TITLE = "updated title";
        TemplateInfo template = templateRepository.findTemplate(TEMPLATE_ID);

        // when
        template.setTitle(UPDATED_TITLE);
        templateRepository.updateTemplate(template);

        TemplateInfo updatedTemplate = templateRepository.findTemplate(TEMPLATE_ID);

        // then
        assertNotNull(updatedTemplate);
        assertEquals(UPDATED_TITLE, updatedTemplate.getTitle());
    }

    @Test
    @DisplayName("템플릿 삭제")
    void deleteTemplate() {
        // given
        final Long TEMPLATE_ID = 1L;

        // when
        TemplateInfo existedTemplate = templateRepository.findTemplate(TEMPLATE_ID);
        templateRepository.deleteTemplate(TEMPLATE_ID);

        // then
        assertNotNull(existedTemplate);
        assertThrows(TemplateInfraException.class, () -> {
            templateRepository.findTemplate(TEMPLATE_ID);
        });
    }
}