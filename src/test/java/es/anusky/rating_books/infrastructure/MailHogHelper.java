package es.anusky.rating_books.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class MailHogHelper {

    private static final String MAILHOG_API = "http://localhost:8025/api/v2/messages";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean containsEmailTo(String expectedRecipient) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(MAILHOG_API, String.class);
        String body = response.getBody();

        JsonNode root = objectMapper.readTree(body);
        for (JsonNode item : root.get("items")) {
            JsonNode to = item.get("Content").get("Headers").get("To");
            if (to != null && to.isArray()) {
                for (JsonNode email : to) {
                    if (email.asText().contains(expectedRecipient)) return true;
                }
            }
        }
        return false;
    }

    public static boolean containsAliasOnBody(String alias) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(MAILHOG_API, String.class);
        String body = response.getBody();

        JsonNode root = objectMapper.readTree(body);
        for (JsonNode item : root.get("items")) {
            JsonNode contentBody = item.get("Content").get("Body");
            if (contentBody != null && contentBody.asText().contains(alias)) {
                return true;
            }
        }
        return false;
    }

    public static Optional<String> getEmailBody(String expectedEmail) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(MAILHOG_API, String.class);
        String body = response.getBody();

        JsonNode root = objectMapper.readTree(body);
        for (JsonNode item : root.get("items")) {
            JsonNode to = item.get("Content").get("Headers").get("To");
            if (to != null && to.isArray()) {
                for (JsonNode email : to) {
                    if (email.asText().contains(expectedEmail)) {
                        JsonNode bodyNode = item.get("Content").get("Body");
                        if (bodyNode != null) {
                            return Optional.of(bodyNode.asText());
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

}

