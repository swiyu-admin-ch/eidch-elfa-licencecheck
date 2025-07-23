package ch.admin.astra.vz.lc.core.logging.interceptor;

import ch.admin.astra.vz.lc.core.logging.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogInterceptorTest {
    @Mock
    private LoggingService loggingService;

    private LogInterceptor interceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Mock
    private ModelAndView modelAndView;

    @BeforeEach
    void setUp() {
        interceptor = new LogInterceptor(loggingService);
    }

    @Test
    void postHandle_shouldLogOperationFinishedWithSuccess() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/v1/verification");

        // When
        interceptor.postHandle(request, response, handler, modelAndView);

        // Then
        verify(loggingService).logOperationFinished();
    }


    @Test
    void afterCompletion_shouldClearMDC() {
        // When
        interceptor.afterCompletion(request, response, handler, null);

        // Then
        verify(loggingService).clearMDC();
    }

    @Test
    void afterCompletion_shouldStillClearMDC_whenExceptionIsThrown() {
        Exception exception = new RuntimeException("something went wrong");

        // When
        interceptor.afterCompletion(request, response, handler, exception);

        // Then
        verify(loggingService).clearMDC();
    }
}