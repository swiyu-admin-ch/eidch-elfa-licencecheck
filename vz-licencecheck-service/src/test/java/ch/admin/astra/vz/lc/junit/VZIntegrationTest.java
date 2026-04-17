package ch.admin.astra.vz.lc.junit;

import ch.admin.astra.vz.lc.Application;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

/**
 * Meta-annotation that can be specified on a test class that runs Spring Boot
 * based integration tests. Provides the following features:
 * <ul>
 *   <li>
 *     it applies the {@link SpringBootTest} annotation, and configures the Webserver
 *     with a mock servlet environment
 *   <li>
 *     it enables and configures autoconfiguration of the <tt>MockMvc</tt>.
 *   <li>
 *     it excludes the {@link OAuth2ClientAutoConfiguration}
 *   <li>
 *     it registers the {@link MockitoExtension} for the usage of mocks and stubbings
 *   <li>
 *     it activates the <cite>test</cite> Spring profile
 * </ul>
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public @interface VZIntegrationTest {
}
