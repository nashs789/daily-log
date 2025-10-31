package com.nashs.daily_log.api.template.controller;

import com.nashs.daily_log.api.template.request.TemplateSaveRequest;
import com.nashs.daily_log.api.template.response.TemplateSaveResponse;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.template.service.TemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/template")
public class TemplateRestController {

    private final TemplateService templateService;

    @PutMapping
    public ResponseEntity<TemplateSaveResponse> test(
            @Valid @RequestBody TemplateSaveRequest templateSaveRequest,
            LifeLogUser lifeLogUser
    ) {
        log.info("templateSaveRequest = {}", templateSaveRequest);
        log.info("user = {}", lifeLogUser);

        TemplateInfo templateInfo = templateSaveRequest.toInfo();
        templateInfo.setUserInfo(lifeLogUser.toInfo());

        TemplateInfo info = templateService.saveTemplate(templateInfo);

        log.info("info = {}", info);

        return ResponseEntity.ok(new TemplateSaveResponse());
    }
}
