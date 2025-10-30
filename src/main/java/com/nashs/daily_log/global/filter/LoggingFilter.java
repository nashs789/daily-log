package com.nashs.daily_log.global.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (!(req instanceof HttpServletRequest) || !(res instanceof HttpServletResponse)) {
            chain.doFilter(req, res);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) req);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) res);

        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logRequest(wrappedRequest);
            logResponse(wrappedResponse);
            wrappedResponse.copyBodyToResponse();
        }
    }

    // TODO - 로깅 debug 로 고민 해보자(1)
    private void logRequest(ContentCachingRequestWrapper req) {
        String body = getBody(req.getContentAsByteArray(), req.getCharacterEncoding());
        log.info("HTTP Request - Method: {}, URI: {}, Body: {}",
                 req.getMethod(),
                 req.getRequestURI(),
                 body);
    }

    // TODO - 로깅 debug 로 고민 해보자(2)
    private void logResponse(ContentCachingResponseWrapper res) {
        String body = getBody(res.getContentAsByteArray(), res.getCharacterEncoding());
        log.debug("HTTP Response - Status: {}, Body: {}",
                 res.getStatus(),
                 body);
    }

    private String getBody(byte[] content, String encoding) {
        try {
            return new String(content, encoding);
        } catch (Exception e) {
            return "[Unsupported Encoding]";
        }
    }
}
