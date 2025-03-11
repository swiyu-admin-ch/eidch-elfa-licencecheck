package ch.admin.astra.vz.lc.domain.vam.configuration;

import ch.admin.astra.vz.lc.domain.vam.api.VerifierAgentManagementApi;
import ch.admin.astra.vz.lc.domain.vam.configuration.interceptor.VAMHeaderInterceptor;
import ch.admin.astra.vz.lc.domain.vam.configuration.interceptor.VAMLoggingInterceptor;
import ch.admin.astra.vz.lc.domain.vam.model.ErrorCodeDto;
import ch.admin.astra.vz.lc.domain.vam.service.MockVerifierAgentManagementImpl;
import ch.admin.astra.vz.lc.domain.vam.service.VerifierAgentManagementClient;
import ch.admin.astra.vz.lc.domain.vam.service.VerifierAgentManagementImpl;
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
    public VerifierAgentManagementClient mockVerifierAgentManagementImpl(@Value("${verifier.mock.failResponse}") Boolean failResponse, @Value("${verifier.mock.errorCode}") ErrorCodeDto errorCode) {
        return new MockVerifierAgentManagementImpl(failResponse, errorCode);
    }
}