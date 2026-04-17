package ch.admin.astra.vz.lc.core.util;

import ch.admin.astra.vz.lc.modules.verification.exception.ImageHandlingException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@UtilityClass
public class ImageUtils {

    public static final String IMAGE_DIRECTORY = "images/";

    public static byte[] loadCategoryIcon(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("filePath must not be null");
        }
        ClassPathResource imgFile = new ClassPathResource(IMAGE_DIRECTORY + filePath);

        try {
            return imgFile.getContentAsByteArray();
        } catch (IOException e) {
            throw new ImageHandlingException("Failed to load categoryIcon for category: " + filePath, e);
        }
    }

    public static String convertToBase64(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return null;
        }

        try {
            byte[] imageBytes = loadCategoryIcon(filePath);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            log.warn("Failed to convert image to base64 for path: {}", filePath, e);
            return null;
        }
    }
}
