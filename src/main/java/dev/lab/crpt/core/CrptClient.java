package dev.lab.crpt.core;

import java.io.IOException;
import java.net.http.HttpResponse;

public interface CrptClient {
    HttpResponse<String> createDocument(Object document, String signature) throws IOException, InterruptedException;
}
