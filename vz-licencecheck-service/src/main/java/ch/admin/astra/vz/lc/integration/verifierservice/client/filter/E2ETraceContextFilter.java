package ch.admin.astra.vz.lc.integration.verifierservice.client.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Copies the traceparent header from request to response.
 * automatically detected by SpringBoot
 * <a href="https://www.w3.org/TR/trace-context/">...</a>
 */
@Order(1)
@Component
@Slf4j
public class E2ETraceContextFilter extends OncePerRequestFilter {

    public static final String TRACEPARENT = "traceparent";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        log.trace("Request: {} {} Headers: {}",
                request.getMethod(), request.getRequestURI(),
                request.getHeaderNames());

        response.setHeader(TRACEPARENT, request.getHeader(TRACEPARENT));
        filterChain.doFilter(request, response);
    }

}


