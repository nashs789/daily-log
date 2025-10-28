package com.nashs.daily_log.api;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(LifeLogUser user) {
        log.info("user:{}", user);
        return "home"; // /WEB-INF/views/home.jsp
    }
}
