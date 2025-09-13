package dev.lab.crpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lab.crpt.core.JsonCodec;
import dev.lab.crpt.core.RateLimiter;
import dev.lab.crpt.core.Transport;
import dev.lab.crpt.impl.DefaultCrptClient;
import dev.lab.crpt.impl.JacksonJsonCodec;
import dev.lab.crpt.impl.SlidingWindowRateLimiter;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RateLimiterUnitTest {

    static class NoopTransport implements Transport {
        @Override
        @SuppressWarnings("unchecked")
        public HttpResponse<String> send(HttpRequest request) {
            HttpResponse<String> resp = (HttpResponse<String>) mock(HttpResponse.class);
            when(resp.statusCode()).thenReturn(200);
            when(resp.body()).thenReturn("{\"status\":\"ok\"}");
            return resp;
        }
    }

    @Test
    void thirdCallWaits_whenLimit2PerSecond() throws Exception {
        RateLimiter limiter = new SlidingWindowRateLimiter(2, TimeUnit.SECONDS);
        JsonCodec codec = new JacksonJsonCodec(new ObjectMapper());
        Transport transport = new NoopTransport();

        DefaultCrptClient client = new DefaultCrptClient(
                transport,
                codec,
                URI.create("http://localhost"),
                Duration.ofSeconds(2),
                limiter
        );

        Runnable call = () -> {
            try {
                client.createDocument(Map.of("k", "v"), "sig");
            } catch (RuntimeException | IOException | InterruptedException e) {
                fail("Unexpected runtime error: " + e.getMessage());
            }
        };

        long start = System.nanoTime();
        var pool = Executors.newFixedThreadPool(3);
        pool.submit(call);
        pool.submit(call);
        pool.submit(call);
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        long ms = (System.nanoTime() - start) / 1_000_000;
        assertThat(ms).isGreaterThanOrEqualTo(1000);
    }
}