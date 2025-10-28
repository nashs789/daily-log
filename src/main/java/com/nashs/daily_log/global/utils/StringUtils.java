package com.nashs.daily_log.global.utils;

import java.util.Objects;

public class StringUtils {

    private StringUtils() {}

    public static String emptyToNull(String str) {
        return str == null || str.isBlank() ? "" : str;
    }

    public static String nullOrTrim(String s) {
        return Objects.isNull(s) || s.trim().isEmpty() ? null : s.trim();
    }

    public static String nullSafe(String s, String dft) {
        return io.micrometer.common.util.StringUtils.isEmpty(s) ? dft : s;
    }
}
