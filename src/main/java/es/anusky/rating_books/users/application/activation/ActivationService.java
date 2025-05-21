package es.anusky.rating_books.users.application.activation;

import es.anusky.rating_books.infrastructure.exception.TokenAlreadyUsedException;
import es.anusky.rating_books.infrastructure.exception.TokenExpiredException;
import es.anusky.rating_books.infrastructure.exception.TokenNotFoundException;
import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import es.anusky.rating_books.users.infrastructure.persistence.ActivationTokenEntity;
import es.anusky.rating_books.users.infrastructure.persistence.SpringDataActivationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivationService {

    private final SpringDataActivationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void activate(String token) {
        ActivationTokenEntity existing = tokenRepository.findByToken(token).orElse(null);
        if (existing == null) {
            throw new TokenNotFoundException("Token not found");
        }
        if (existing.isUsed()) {
            throw new TokenAlreadyUsedException("Token already used");
        }
        if (existing.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired");
        }

        User user = userRepository.findById(existing.getUser().getId()).orElseThrow(
                () -> new UserNotFoundException("User not found while trying to active it!")
        );
        var saved = userRepository.update(user.activate());
        eventPublisher.publishEvent(buildAuditEvent(saved));
        existing.setUsed(true);
        tokenRepository.save(existing);

    }

    private AuditEvent buildAuditEvent(User saved) {
        return new AuditEvent(LocalDateTime.now(),
                Entities.USER.name(),
                saved.getUserId().getValue(),
                Actions.UPDATE.name(),
                saved.getAlias().getValue(),
                "User activated");
    }
}
