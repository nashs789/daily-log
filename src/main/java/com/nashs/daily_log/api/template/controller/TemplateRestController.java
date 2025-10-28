package com.nashs.daily_log.api.template.controller;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/template")
public class TemplateRestController {

    @PutMapping
    public ResponseEntity<String> test(LifeLogUser user) {
        return ResponseEntity.ok("hi");
    }
}
