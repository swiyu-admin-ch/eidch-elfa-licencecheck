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
import java.util.regex.Pattern;

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

    /**
     * W3C trace-context {@code traceparent} format: {@code version-traceId-parentId-flags}
     * (see <a href="https://www.w3.org/TR/trace-context/#traceparent-header-field-values">spec</a>).
     * Validating against it before echoing the value prevents HTTP response splitting (CWE-113),
     * since a well-formed value cannot contain CR/LF or other injection characters.
     */
    private static final Pattern TRACEPARENT_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{2}-[0-9a-fA-F]{32}-[0-9a-fA-F]{16}-[0-9a-fA-F]{2}$");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        log.trace("Request: {} {} Headers: {}",
                request.getMethod(), request.getRequestURI(),
                request.getHeaderNames());

        String traceparent = request.getHeader(TRACEPARENT);
        if (traceparent != null) {
            // Strip any CR/LF first (defuses HTTP response splitting, CWE-113), then accept the value
            // only if it matches the strict W3C traceparent format before echoing it back.
            String sanitized = traceparent.replaceAll("[\\r\\n]", "");
            if (TRACEPARENT_PATTERN.matcher(sanitized).matches()) {
                response.setHeader(TRACEPARENT, sanitized);
            }
        }
        filterChain.doFilter(request, response);
    }

}


