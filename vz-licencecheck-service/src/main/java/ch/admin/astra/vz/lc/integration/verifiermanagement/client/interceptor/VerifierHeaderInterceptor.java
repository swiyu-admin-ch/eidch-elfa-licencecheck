package ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class VerifierHeaderInterceptor implements ClientHttpRequestInterceptor {

    public static final String ACCEPT = "accept";
    public static final String CONTENT_TYPE = "Content-Type";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.set(ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return execution.execute(request, body);
    }
}