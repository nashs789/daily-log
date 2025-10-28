package com.nashs.daily_log.api.template.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/template")
public class TemplateApiController {

    @PutMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("hi");
    }
}
