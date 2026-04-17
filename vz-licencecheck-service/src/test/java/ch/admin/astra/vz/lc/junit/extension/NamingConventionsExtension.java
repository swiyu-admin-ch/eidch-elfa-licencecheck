package ch.admin.astra.vz.lc.junit.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;

import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

/**
 * JUnit extension, which checks the naming conventions of test classes.
 */
public class NamingConventionsExtension implements BeforeAllCallback {

    private static final Pattern UNIT_TEST_PATTERN = Pattern.compile(".*Test");
    private static final Pattern INTEGRATION_TEST_PATTERN = Pattern.compile(".*(Test)?IT");

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if the top-level declaring class doesn't match the naming conventions.
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        final var testClass = context.getRequiredTestClass();
        if (testClass.getDeclaringClass() == null) {
            final var isIntegrationTest = isIntegrationTest(context);
            final var isUnitTest = !isIntegrationTest;

            if (isIntegrationTest && !matchesIntegrationTestPattern(testClass)) {
                throw new IllegalStateException(String.format(
                    "The name of integration-test classes must match the regex pattern '%s'",
                    INTEGRATION_TEST_PATTERN.pattern()
                ));
            } else if (isUnitTest && !matchesUnitTestPattern(testClass)) {
                throw new IllegalStateException(String.format(
                    "The name of unit-test classes must match the regex pattern '%s'",
                    UNIT_TEST_PATTERN.pattern()
                ));
            }
        }
    }

    private static boolean matchesIntegrationTestPattern(Class<?> testClass) {
        return INTEGRATION_TEST_PATTERN.matcher(testClass.getName()).matches();
    }

    private static boolean matchesUnitTestPattern(Class<?> testClass) {
        return UNIT_TEST_PATTERN.matcher(testClass.getName()).matches();
    }

    /**
     * All tests which are annotated with {@link SpringBootTest} are integration tests.
     *
     * @param context JUnit's extension context.
     * @return <tt>true</tt> if the test class represents an integration test.
     */
    private static boolean isIntegrationTest(ExtensionContext context) {
        return findAnnotation(context.getRequiredTestClass(), SpringBootTest.class).isPresent();
    }
}
