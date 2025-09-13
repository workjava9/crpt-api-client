package dev.lab.crpt.impl;

import dev.lab.crpt.core.CrptClient;
import dev.lab.crpt.core.JsonCodec;
import dev.lab.crpt.core.RateLimiter;
import dev.lab.crpt.core.Transport;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultCrptClient implements CrptClient {

    private final Transport transport;
    private final JsonCodec json;
    private final URI baseUri;
    private final Duration timeout;
    private final RateLimiter limiter;



    @Override
    public HttpResponse<String> createDocument(Object document, String signature) throws IOException, InterruptedException {
        if (document == null) throw new IllegalArgumentException("document is null");
        if (signature == null) throw new IllegalArgumentException("signature is null");

        limiter.acquire();

        String body = json.toJson(Map.of("document", document, "signature", signature));
        URI uri = baseUri.resolve("/api/v3/lk/documents/create");

        HttpRequest req = HttpRequest.newBuilder(uri)
                .timeout(timeout)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return transport.send(req);
    }
}
