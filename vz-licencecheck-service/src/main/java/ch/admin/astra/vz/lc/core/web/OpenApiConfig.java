package ch.admin.astra.vz.lc.core.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                version = "1.0.0",
                title = "LicenceCheck Service API",
                description = "The API to start a verification process, load use-cases and poll for verification status. "
        )
)
@Configuration
public class OpenApiConfig {

    @Bean
    GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("API")
                .pathsToMatch("/api/**", "/actuator/**")
                .build();
    }
}
