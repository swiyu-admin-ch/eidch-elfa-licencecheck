package ch.admin.astra.vz.lc.core.logging.interceptor;

import ch.admin.astra.vz.lc.core.logging.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final List<String> LOGGED_APIS = List.of(
            "/api/v1/verification");

    private final LoggingService loggingService;

    /**
     * The postHandle will log the actual operation with all the added context in case no Exceptions occurred.
     * In case of all Exceptions, we call the logException method manually via DefaultExceptionHandler.
     * In case of BusinessExceptions we log the operation normally with a ERROR status as info and as debug in detail.
     * Any other Exceptions get logged as error log level.
     */
    @Override
    public void postHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler,
            ModelAndView modelAndView
    ) {
        if (LOGGED_APIS.stream().anyMatch(path -> request.getRequestURI().startsWith(path))) {
            loggingService.logOperationFinished();
        }
    }

    /**
     * Clears the MDC in all cases, even if an exception occurs.
     */
    @Override
    public void afterCompletion(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler,
            Exception ex
    ) {
        loggingService.clearMDC();
    }

}