package com.nashs.daily_log.global.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtils {

    private CookieUtils() {}

    public static String readCookieByName(HttpServletRequest req, String name) {
        Cookie[] cs = req.getCookies();
        if (cs == null) return null;

        for (Cookie c : cs) {
            if (name.equals(c.getName())) {
                return c.getValue();
            }
        }

        return null;
    }
}
