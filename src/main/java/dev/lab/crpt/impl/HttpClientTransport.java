package dev.lab.crpt.impl;

import dev.lab.crpt.core.Transport;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RequiredArgsConstructor
public class HttpClientTransport implements Transport {

    private final HttpClient http;

    @Override
    public HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        return http.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
