package com.nashs.daily_log.global.props;

import com.nashs.daily_log.api.template.props.UrlProps;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(annotations = Controller.class)
public class ViewAttributeProps {

    private final UrlProps urlProps;

    @ModelAttribute("lifelog")
    public Map<String, Object> lifelogAttributes(HttpServletRequest req) {
        return Map.of("app", urlProps);
    }
}
