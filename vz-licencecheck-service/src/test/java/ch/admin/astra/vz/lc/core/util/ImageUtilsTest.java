package ch.admin.astra.vz.lc.core.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageUtilsTest {

    @Test
    void convertToBase64_shouldReturnBase64String_whenValidImagePath() {
        // Given
        String imageFilePath = "category-icons/kat_a.png";

        // When
        String result = ImageUtils.convertToBase64(imageFilePath);

        // Then
        assertThat(result)
            .isNotNull()
            .isNotEmpty()
            .matches("^[A-Za-z0-9+/]*={0,2}$");
    }

    @Test
    void convertToBase64_shouldReturnNull_whenNullPath() {
        // When
        @SuppressWarnings("ConstantValue")
        String result = ImageUtils.convertToBase64(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void convertToBase64_shouldReturnNull_whenEmptyPath() {
        // When
        String result = ImageUtils.convertToBase64("");

        // Then
        assertThat(result).isNull();
    }

    @Test
    void convertToBase64_shouldReturnNull_whenInvalidPath() {
        // Given
        String invalidImageFilePath = "non-existent/path.png";

        // When
        String result = ImageUtils.convertToBase64(invalidImageFilePath);

        // Then - Should return null when image file doesn't exist (handled gracefully)
        assertThat(result).isNull();
    }
}
