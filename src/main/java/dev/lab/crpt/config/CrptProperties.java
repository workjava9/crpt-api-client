package dev.lab.crpt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Setter
@Getter
@ConfigurationProperties(prefix = "crpt")
public class CrptProperties {
    private String baseUrl;
    private Duration timeout = Duration.ofSeconds(5);
    private RateLimit rateLimit = new RateLimit();

    @Setter
    @Getter
    public static class RateLimit {
        private String unit = "SECONDS";
        private int limit = 5;

    }

}
