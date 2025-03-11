package ch.admin.astra.vz.lc.logging.filter;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * All requests and error responses (even 404, 500) are intercepted by GlobalFilter since it wraps request execution.
 */
@Component
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        MDC.clear();
    }
}