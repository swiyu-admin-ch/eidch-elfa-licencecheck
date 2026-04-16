package ch.admin.astra.vz.lc.core.features;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "features")
public class FeaturesProperties {

    private boolean useSingleVerifierEnabled;
}