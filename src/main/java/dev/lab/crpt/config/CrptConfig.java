package dev.lab.crpt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.lab.crpt.core.JsonCodec;
import dev.lab.crpt.core.RateLimiter;
import dev.lab.crpt.core.Transport;
import dev.lab.crpt.impl.HttpClientTransport;
import dev.lab.crpt.impl.JacksonJsonCodec;
import dev.lab.crpt.impl.SlidingWindowRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(CrptProperties.class)
public class CrptConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        return m;
    }

    @Bean
    public Transport transport() {
        return new HttpClientTransport(
                HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(10))
                        .build()
        );
    }

    @Bean
    public JsonCodec jsonCodec(ObjectMapper mapper) {
        return new JacksonJsonCodec(mapper);
    }

    @Bean
    public RateLimiter rateLimiter(CrptProperties props) {
        TimeUnit unit = TimeUnit.valueOf(props.getRateLimit().getUnit());
        return new SlidingWindowRateLimiter(
                props.getRateLimit().getLimit(),
                unit
        );
    }

    @Bean
    public dev.lab.crpt.core.CrptClient crptClient(
            Transport transport,
            JsonCodec jsonCodec,
            RateLimiter limiter,
            CrptProperties props
    ) {
        return new dev.lab.crpt.impl.DefaultCrptClient(
                transport,
                jsonCodec,
                URI.create(props.getBaseUrl()),
                props.getTimeout(),
                limiter
        );
    }
}
