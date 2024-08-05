package io.hhplus.concert.interfaces.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String uuid = UUID.randomUUID().toString();

        // request 로그 처리
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        String requestUrl = request.getRequestURI();
        String httpMethod = request.getMethod();
        String requestBody = requestWrapper.getContentAsString();

        log.info("[ID : {}][METHOD : {}][URL : {}]", uuid, httpMethod, requestUrl);
        log.info("[REQUEST BODY : {}]", requestBody);

        filterChain.doFilter(servletRequest, servletResponse);

        // response 로그 처리
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String responseBody = new String(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
        log.info("[ID : {}][METHOD : {}][URL : {}]", uuid, httpMethod, requestUrl);
        log.info("[RESPONSE BODY : {}]", responseBody);

        responseWrapper.copyBodyToResponse();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
