package ch.admin.astra.vz.lc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {

        Environment env = SpringApplication.run(Application.class, args).getEnvironment();
        String appName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");

        log.info(
                """
    
    ----------------------------------------------------------------------------
    \t'{}' is running!\s
    \tProfile(s):\t\t\t\t{}
    \tFrontend (node):\t\thttp://localhost:4201
    \tFrontend (bundled):\t\thttp://localhost:8888
    \tSwaggerUI:\t\t\t\thttp://localhost:{}/swagger-ui.html
    ----------------------------------------------------------------------------""",
                appName,
                env.getActiveProfiles(),
                serverPort
        );
    }
}
