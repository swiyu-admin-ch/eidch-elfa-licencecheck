package ch.admin.astra.vz.lc.integration;

import ch.admin.astra.vz.lc.modules.verification.domain.usecase.UseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class UseCaseImageConversionTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldLoadValidityJsonWithImagePaths() throws IOException {
        // Given
        ClassPathResource resource = new ClassPathResource("use-cases/mdl/validity.json");

        // When
        UseCase useCase = objectMapper.readValue(resource.getInputStream(), UseCase.class);

        // Then
        assertThat(useCase).isNotNull();
        assertThat(useCase.getInputElements().getFirst().getType()).isEqualTo("selection");
        assertThat(useCase.getInputElements().getFirst().getName()).isEqualTo("mdlCategory");

        // Verify image paths are properly loaded
        var options = useCase.getInputElements().getFirst().getInputOptions();
        assertThat(options).hasSize(16);

        // Check that the JSON contains file paths
        assertThat(options.get(0).getImageFilePath()).isEqualTo("category-icons/kat_a.png");
        assertThat(options.get(1).getImageFilePath()).isEqualTo("category-icons/kat_a1.png");
        assertThat(options.get(2).getImageFilePath()).isEqualTo("category-icons/kat_b.png");
        assertThat(options.get(3).getImageFilePath()).isEqualTo("category-icons/kat_b1.png");

        log.info("✅ UseCase loaded successfully with image paths:");
        options.forEach(option ->
            log.info("  {} -> {}", option.getKey(), option.getImageFilePath())
        );
    }
}
