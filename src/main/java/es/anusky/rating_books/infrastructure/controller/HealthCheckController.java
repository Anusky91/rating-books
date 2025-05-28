package es.anusky.rating_books.infrastructure.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "HealthCheck")
@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @Value("${spring.application.name}")
    private String name;

    @GetMapping
    public Map<String, String> getHealth() {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("status", "alive");
        return body;
    }
}
