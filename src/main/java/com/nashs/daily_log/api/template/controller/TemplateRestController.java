package com.nashs.daily_log.api.template.controller;

import com.nashs.daily_log.api.template.request.TemplateRequest;
import com.nashs.daily_log.api.template.response.TemplateFindResponse;
import com.nashs.daily_log.api.template.response.TemplateResponse;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.template.service.TemplateService;
import com.nashs.daily_log.global.annotation.LoginRequired;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/template")
public class TemplateRestController {

    private final TemplateService templateService;

    @GetMapping
    public ResponseEntity<List<TemplateFindResponse>> findAllTemplate(LifeLogUser lifeLogUser) {
        return ResponseEntity.ok(templateService.findAllTemplate(lifeLogUser)
                                                .stream()
                                                .map(TemplateFindResponse::fromInfo)
                                                .toList());
    }

    @LoginRequired
    @PatchMapping
    public ResponseEntity<TemplateResponse> updateTemplate(
            @Valid @RequestBody TemplateRequest templateRequest,
            LifeLogUser lifeLogUser
    ) {
        templateService.updateTemplate(lifeLogUser, templateRequest.toInfo()
                                                                   .setupUser(lifeLogUser));

        return ResponseEntity.noContent()
                             .build();
    }

    @LoginRequired
    @PutMapping
    public ResponseEntity<TemplateResponse> saveTemplate(
            @Valid @RequestBody TemplateRequest templateRequest,
            LifeLogUser lifeLogUser
    ) {
        return ResponseEntity.ok(TemplateResponse.fromInfo(
                templateService.saveTemplate(templateRequest.toInfo()
                                                            .setupUser(lifeLogUser))
        ));
    }

    @LoginRequired
    @DeleteMapping("/{templateId}")
    public ResponseEntity<Void> deleteTemplate(
            @PathVariable Long templateId,
            LifeLogUser lifeLogUser
    ) {
        templateService.deleteTemplate(lifeLogUser, templateId);

        return ResponseEntity.noContent()
                             .build();
    }
}
