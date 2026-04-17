package ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Alternatives:
 * - <a href="https://confluence.bit.admin.ch/display/JEAP/Request+Tracing">...</a>
 *   - verwendet io.micrometer
 *   - ausserdem achtet jeap darauf dass keine sensitiven informationen geloggt werden
 *   - jeap kann aber keine response bodies loggen
 *   - Achtung: funktioniert nur für Spring Bean mit RestClient.builder.
 *  --
 * - apache wire logging (sehr gutes lesbarer output)
 *   <a href="https://www.baeldung.com/apache-httpclient-enable-logging">...</a>
 *      [org.apache.http.wire] >> "POST /api/v1/test HTTP/1.1[\r][\n]"
 *      [org.apache.http.wire] >> "Content-Type: application/json[\r][\n]"
 *      [org.apache.http.wire] >> "[\r][\n]"
 *      [org.apache.http.wire] >> "{"request":"data"}"
 *      [org.apache.http.wire] << "HTTP/1.1 200 OK[\r][\n]"
 *      [org.apache.http.wire] << "Content-Type: application/json[\r][\n]"
 *      [org.apache.http.wire] << "[\r][\n]"
 *      [org.apache.http.wire] << "{"response":"data"}"
 * - Spring Feign
 * - Spring Actuator
 * --
 * Reading and logging the HTTP response body with new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8) inside a Spring Boot logging interceptor carries significant risks including security exposure, memory overhead, and altered response behavior.
 * Risks and Problems
 * Sensitive Data Exposure: Request and response bodies can contain secrets, passwords, API keys, or personally identifiable information. Logging these as plain strings can leak confidential data into log files, which may be accessible to unauthorized users or attackers.
 * One-Time Stream Consumption: Most HTTP response streams in Spring can only be read once. Reading all bytes for logging means subsequent code (or client) cannot access the body without wrapping or duplicating the response, which complicates pipeline handling or causes hard-to-debug side effects.
 * Memory Usage: Loading large responses entirely into memory for string conversion can cause resource issues, performance bottlenecks, or even out-of-memory errors when payloads are large or frequent.
 * Compliance and Privacy: Unfiltered logging may violate GDPR or internal security policies if sensitive information is not adequately masked or redacted before logging.
 * Mitigation Strategies
 * Redact or sanitize sensitive fields in the body before logging, using custom logic.
 * Buffer and re-wrap response body if further downstream access is required (for example, with a wrapper like BufferingClientHttpResponseWrapper to enable multiple reads).
 * Avoid logging bodies for large payloads or binary content types, log only safe, truncated, or summarized data.
 * Best Practice
 * Only log what is necessary for debugging, and implement strict controls to prevent accidental disclosure of secrets and sensitive data within HTTP bodies. For systems dealing with confidential or personal data, prefer summaries, truncation, or explicit whitelisting for loggable fields.
 * Quelle u a.
 * <a href="https://stackoverflow.com/questions/33744875/spring-boot-how-to-log-all-requests-and-responses-with-exceptions-in-single-pl">...</a>
 * --
 * Fabian: wir akzeptieren erst mal die Probleme und verbessern später
 * --
 * Probleme mit BODY Logging im Licencecheck start:backend - dev (Header Logging funktioniert):
 *  2025-09-02 09:53:00,238 ERROR [vz-licencecheck-scs,68b6a25b4d482b9c20422d2c2b6aff70,873a526f1e96ad5d] c.a.a.v.l.c.logging.LoggingService - Operation finished with error:  Reason: Cannot invoke "ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto.verificationUrl()" because "response" is null
 *  java.lang.NullPointerException: Cannot invoke "ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto.verificationUrl()" because "response" is null
 *  at ch.admin.astra.vz.lc.modules.verification.service.VerificationService.createVerification(VerificationService.java:81)
 *  at ch.admin.astra.vz.lc.api.verification.controller.VerificationController.startVerificationProcess(VerificationController.java:48)
 * --
 * start:backend - local does not use VerifierClientConfiguration and therefore does not log.
 *    * --
 *  * Sensible Daten
 *  *  - Headers und Body können sensible Daten enthalten. Dies muss man abwägen bevor man zum Debuggen den HttpLogRequestInterceptor einsetzt.
 *  *  - jEAP Logging löst dieses Problem, deswegen wurde es als Alternative aufgenommen.
 */
@NoArgsConstructor
@Component
public class HttpLogRequestInterceptor implements ClientHttpRequestInterceptor {

    Logger log = LoggerFactory.getLogger("http");

    @Value("${verifier.logging.level}")
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
    public ClientHttpResponse intercept(@NotNull HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body, log);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response, log);

        return response;
    }

    public void logRequest(HttpRequest httpRequest, byte[] body, Logger log) {
        if (logNum()>0) log.info(" >> [{}] {}", httpRequest.getMethod(), httpRequest.getURI());
        if (logNum()>1) httpRequest.getHeaders().keySet().forEach((header) ->
                log.info(" >> header {} = {}", header, httpRequest.getHeaders().get(header)));
        if (logNum()>2) log.info(" >> body {}", new String(body));
    }

    public void logResponse(ClientHttpResponse response, Logger log) throws IOException {
        if (logNum()>0) log.info(" << {} {}",response.getStatusCode(), response.getStatusText());
        if (logNum()>1) response.getHeaders().keySet().forEach((header) ->
                log.info(" << header {} = {}", header, response.getHeaders().get(header)));
                // reading body leads to exceptions
                // ich schlage vor eine bestehende logging lösung wie apache wire log oder spring actuator zu verwenden
        if (logNum() > 2) {
            String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
            log.info(" << body {}", body);
        }
    }

}