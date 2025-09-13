package dev.lab.crpt.core;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface Transport {
    HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException;
}
