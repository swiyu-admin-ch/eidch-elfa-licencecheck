package ch.admin.astra.vz.lc.integration;

import ch.admin.astra.vz.lc.BaseIntegrationTest;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.UseCase;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UseCaseImageConversionIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldLoadValidityJsonWithImagePaths() throws IOException {
        // Given
        ClassPathResource resource = new ClassPathResource("use-cases/mdl/validity_single_category.json");

        // When
        UseCase useCase = objectMapper.readValue(resource.getInputStream(), UseCase.class);

        // Then
        assertNotNull(useCase);
        assertEquals("selection", useCase.getInputElements().getFirst().getType());
        assertEquals("mdlCategory", useCase.getInputElements().getFirst().getName());

        // Verify image paths are properly loaded
        var options = useCase.getInputElements().getFirst().getInputOptions();
        assertEquals(16, options.size());

        // Check that the JSON contains file paths
        assertEquals("category-icons/kat_a.png", options.get(0).getImageFilePath());
        assertEquals("category-icons/kat_a1.png", options.get(1).getImageFilePath());
        assertEquals("category-icons/kat_b.png", options.get(2).getImageFilePath());
        assertEquals("category-icons/kat_b1.png", options.get(3).getImageFilePath());

        System.out.println("✅ UseCase loaded successfully with image paths:");
        options.forEach(option ->
            System.out.println("  " + option.getKey() + " -> " + option.getImageFilePath())
        );
    }
}
