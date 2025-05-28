package es.anusky.rating_books.shared.example;

import io.micrometer.core.ipc.http.HttpSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallExternalApi {
    private final WebClient webClient;

    public List<Map<String, Object>> call() {
        log.info("Calling to {} with method {}", "Typicode", HttpSender.Method.GET.name());
        return webClient.get()
                .uri("https://jsonplaceholder.typicode.com/posts")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                .collectList()
                .block();
    }
}
