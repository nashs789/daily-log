package com.nashs.daily_log.global.advice;

import com.nashs.daily_log.global.props.UrlProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(annotations = Controller.class)
public class UrlAdvice {

    private final UrlProps urlProps;

    @ModelAttribute("lifelog")
    public Map<String, Object> lifelogAttributes() {
        return Map.of("app", urlProps);
    }
}
