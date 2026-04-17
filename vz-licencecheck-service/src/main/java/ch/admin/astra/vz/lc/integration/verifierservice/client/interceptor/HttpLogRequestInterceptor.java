package ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Alternatives:
 * - <a href="https://confluence.bit.admin.ch/display/JEAP/Request+Tracing">jeap-request-tracing</a>
 * - verwendet io.micrometer
 * - ausserdem achtet jeap darauf dass keine sensitiven informationen geloggt werden
 * - jeap kann aber keine response bodies loggen
 * - Achtung: funktioniert nur für Spring Bean mit RestClient.builder.
 */
@NoArgsConstructor
@Component
@Slf4j
public class HttpLogRequestInterceptor implements ClientHttpRequestInterceptor {

    @Value("${verifier-service.logging.level}")
    public String logLevel;

    private int logNum() {
        return switch (logLevel) {
            case "BASIC" -> 1;
            case "HEADERS" -> 2;
            case "BODY" -> 3;
            default -> 0;
        };
    }

    @NotNull
    @Override
    public ClientHttpResponse intercept(@NotNull HttpRequest request, byte @NotNull [] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body, log);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response, log);

        return response;
    }

    public void logRequest(HttpRequest httpRequest, byte[] body, Logger log) {
        int level = logNum();
        if (level == 0) return;

        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);

        writer.printf(" >> [%s] %s", httpRequest.getMethod(), httpRequest.getURI());

        if (level > 1) {
            httpRequest.getHeaders().forEach((header, values) ->
                    writer.printf("\n >> header %s = %s", header, values));
        }

        if (level > 2) {
            writer.printf("\n >> body %s", new String(body, StandardCharsets.UTF_8));
        }

        writer.flush();
        log.debug(buffer.toString());
    }

    public void logResponse(ClientHttpResponse response, Logger log) throws IOException {
        int level = logNum();
        if (level == 0) return;

        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);

        writer.printf(" << %s %s", response.getStatusCode(), response.getStatusText());

        if (level > 1) {
            response.getHeaders().forEach((header, values) ->
                    writer.printf("\n << header %s = %s", header, values));
        }

        if (level > 2) {
            String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
            writer.printf("\n << body %s", body);
        }

        writer.flush();
        log.debug(buffer.toString());
    }

}