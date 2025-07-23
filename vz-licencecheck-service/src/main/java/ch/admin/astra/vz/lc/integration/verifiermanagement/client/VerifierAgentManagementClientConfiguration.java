package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor.VAMHeaderInterceptor;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor.VAMLoggingInterceptor;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.VerificationErrorResponseCodeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@RequiredArgsConstructor
public class VerifierAgentManagementClientConfiguration {

    private final ObjectMapper objectMapper;

    private final VAMHeaderInterceptor vamHeaderInterceptor;
    private final VAMLoggingInterceptor vamLoggingInterceptor;

    @Profile("!local")
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION)
    public VerifierAgentManagementClient verifierAgentManagementImpl(@Value("${verifier-agent-management.endpoint}") String endpoint) {

        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .addNetworkInterceptor(vamHeaderInterceptor)
                        .addInterceptor(vamLoggingInterceptor.getInterceptor())
                        .build())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(endpoint)
                .build();


        VerifierAgentManagementApi api = retrofit.create(VerifierAgentManagementApi.class);
        return new VerifierAgentManagementImpl(api, objectMapper);
    }

    @Profile("local")
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION)
    public VerifierAgentManagementClient mockVerifierAgentManagementImpl(@Value("${verifier.mock.failResponse}") Boolean failResponse, @Value("${verifier.mock.errorCode}") VerificationErrorResponseCodeDto errorCode) {
        return new MockVerifierAgentManagementImpl(failResponse, errorCode);
    }
}