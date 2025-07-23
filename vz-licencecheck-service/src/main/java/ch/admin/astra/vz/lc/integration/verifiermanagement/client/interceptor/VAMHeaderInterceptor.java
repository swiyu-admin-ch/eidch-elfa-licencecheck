package ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor;

import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The VAMHeaderInterceptor class is responsible for adding the necessary headers
 * to the outgoing requests before they are sent to the Verifier Agent Management.
 */
@Component
@RequiredArgsConstructor
public class VAMHeaderInterceptor implements Interceptor {

    public static final String ACCEPT = "accept";
    public static final String CONTENT_TYPE = "Content-Type";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = buildRequest(chain);

        return chain.proceed(request);
    }

    private Request buildRequest(Chain chain) {
        return chain.request()
                .newBuilder()
                .header(ACCEPT, MediaType.APPLICATION_JSON.toString())
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .build();
    }
}