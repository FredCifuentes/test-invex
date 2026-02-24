package com.invex.testinvex.employee.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long start = System.currentTimeMillis();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullPath = queryString == null ? uri : uri + "?" + queryString;

        // Headers
        String userAgent = request.getHeader("User-Agent");
        String contentType = request.getHeader("Content-Type");
        String requestId = request.getHeader("X-Request-Id");

        log.info("Incoming request -> method={}, path={}, contentType={}, userAgent={}, xRequestId={}",
                method, fullPath, contentType, userAgent, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - start;
            log.info("Outgoing response -> method={}, path={}, status={}, durationMs={}",
                    method, fullPath, response.getStatus(), durationMs);
        }
    }
}
