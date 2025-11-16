package com.nashs.daily_log.api.webhook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(("/webhook/history"))
public class WebhookController {

    @GetMapping
    public ModelAndView historyPage(
            ModelAndView mv
    ) {

        mv.setViewName("webhook/history");

        return mv;
    }
}
