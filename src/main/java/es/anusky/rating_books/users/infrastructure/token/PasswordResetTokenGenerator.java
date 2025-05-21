package es.anusky.rating_books.users.infrastructure.token;

import es.anusky.rating_books.users.infrastructure.persistence.PasswordResetTokenEntity;
import es.anusky.rating_books.users.infrastructure.persistence.SpringDataPasswordResetTokenRepository;
import es.anusky.rating_books.users.infrastructure.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenGenerator {

    private final SpringDataPasswordResetTokenRepository repository;

    public String generate(UserEntity user) {
        LocalDateTime now = LocalDateTime.now();
        PasswordResetTokenEntity token = PasswordResetTokenEntity.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(now)
                .expiresAt(now.plusHours(1L))
                .used(false).build();
        var saved = repository.save(token);
        return saved.getToken();
    }

    /**
     * If exists a token available we set it to used=true and generate a new one.
     * @param user user entity
     * @return new token
     */
    public String generateNewToken(UserEntity user) {
        LocalDateTime now = LocalDateTime.now();
        PasswordResetTokenEntity existing = repository.findByUserAndUsedFalse(user).orElse(null);
        if (existing != null && existing.getExpiresAt().isAfter(now)) {
            existing.setUsed(true);
            repository.save(existing);
        }
        return generate(user);
    }
}
