package dev.lab.crpt;

import dev.lab.crpt.core.CrptClient;
import dev.lab.crpt.core.InputRuDocument;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final CrptClient client;

    public Application(CrptClient client) {
        this.client = client;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (System.getenv("DEMO_CALL") != null) {
            var doc = new InputRuDocument(
                    "LP_INTRODUCE_GOODS",
                    "7700000000",
                    "2025-01-01",
                    List.of(Map.of("gtin", "04601234567890", "serial", "A1"))
            );
            var resp = client.createDocument(doc, "sig");
            System.out.println("HTTP " + resp.statusCode() + " => " + resp.body());
        }
    }
}
