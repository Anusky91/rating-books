package es.anusky.rating_books.users.infrastructure.token;

import es.anusky.rating_books.users.infrastructure.persistence.ActivationTokenEntity;
import es.anusky.rating_books.users.infrastructure.persistence.SpringDataActivationTokenRepository;
import es.anusky.rating_books.users.infrastructure.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivationTokenGenerator {

    private final SpringDataActivationTokenRepository repository;

    public String generate(UserEntity user) {
        LocalDateTime now = LocalDateTime.now();
        ActivationTokenEntity token = ActivationTokenEntity.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(now)
                .expiresAt(now.plusDays(1L))
                .used(false).build();
        var saved = repository.save(token);
        return saved.getToken();
    }

    /**
     * If exists an active token available we set it to used=true and generate a new one.
     * @param user user entity
     * @return new token
     */
    public String generateNewToken(UserEntity user) {
        ActivationTokenEntity existing = repository.findByUserAndUsedFalse(user).orElse(null);
        if (existing != null && existing.getExpiresAt().isAfter(LocalDateTime.now())) {
            existing.setUsed(true);
            repository.save(existing);
        }
        return generate(user);
    }
}
