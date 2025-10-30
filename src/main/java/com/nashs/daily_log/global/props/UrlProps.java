package com.nashs.daily_log.global.props;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "url")
public class UrlProps {
    private String base;
    private String jsp;
    private String css;
    private String image;
    private Script script;

    @Getter
    @Setter
    public static class Script {
        private String jquery;
        /* 마크업 파서 */
        private String marked;
        /*  xss 세이프 가드 */
        private String safeGuard;
    }
}
