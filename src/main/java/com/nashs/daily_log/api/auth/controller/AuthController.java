package com.nashs.daily_log.api.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        System.out.print("login!!!!!!");
        return "auth/login";
    }
}
