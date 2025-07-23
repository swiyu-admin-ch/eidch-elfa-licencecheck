package ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor;

import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VAMLoggingInterceptor {

    @Value("${verifier-agent-management.logging.level}")
    private HttpLoggingInterceptor.Level level;

    public HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(level);
        return httpLoggingInterceptor;
    }
}
