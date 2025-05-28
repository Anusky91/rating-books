package es.anusky.rating_books.shared.example;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/call")
@RequiredArgsConstructor
public class CallExternalApiController {

    private final CallExternalApi service;

    @GetMapping
    public List<Map<String, Object>> call() {
        return service.call();
    }
}
