package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import ch.admin.astra.vz.lc.core.features.FeaturesProperties;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor.VerifierHeaderInterceptor;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor.VerifierLoggingInterceptor;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.VerificationErrorResponseCodeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@RequiredArgsConstructor
public class VerifierClientConfiguration {

    private final ObjectMapper objectMapper;
    private final FeaturesProperties features;

    private final VerifierHeaderInterceptor VerifierHeaderInterceptor;
    private final VerifierLoggingInterceptor verifierLoggingInterceptor;

    @Profile("!local")
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public VerifierServiceClient verifierManagementImpl(@Value("${verifier-service.endpoint}") String endpoint) {
        OkHttpClient okHttpClient = buildOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(endpoint)
                .build();

        VerifierApi api = createVerifierApi(retrofit);
        return new VerifierServiceImpl(api, objectMapper);
    }

    @Profile("local")
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public VerifierServiceClient mockVerifierManagementImpl(@Value("${verifier.mock.failResponse}") Boolean failResponse, @Value("${verifier.mock.errorCode}") VerificationErrorResponseCodeDto errorCode) {
        return new MockVerifierServiceImpl(failResponse, errorCode);
    }

    /**
     * Builds and configures OkHttpClient with the required interceptors.
     *
     * @return configured OkHttpClient instance
     */
    protected OkHttpClient buildOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addNetworkInterceptor(VerifierHeaderInterceptor)
                .addInterceptor(verifierLoggingInterceptor.getInterceptor());

        return clientBuilder.build();
    }

    /**
     * Creates the appropriate VerifierApi implementation based on configuration.
     *
     * @param retrofit the Retrofit instance to create the API with
     * @return the VerifierApi implementation
     */
    protected VerifierApi createVerifierApi(Retrofit retrofit) {
        if (features.isUseSingleVerifierEnabled()) {
            return retrofit.create(VerifierServiceApi.class);
        } else {
            return retrofit.create(VerifierManagementApi.class);
        }
    }
}