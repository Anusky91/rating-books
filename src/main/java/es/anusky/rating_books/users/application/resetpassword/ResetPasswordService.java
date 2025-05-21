package es.anusky.rating_books.users.application.resetpassword;

import es.anusky.rating_books.infrastructure.exception.TokenAlreadyUsedException;
import es.anusky.rating_books.infrastructure.exception.TokenExpiredException;
import es.anusky.rating_books.infrastructure.exception.TokenNotFoundException;
import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import es.anusky.rating_books.users.domain.valueobjects.Password;
import es.anusky.rating_books.users.infrastructure.mail.EmailService;
import es.anusky.rating_books.users.infrastructure.mapper.UserMapper;
import es.anusky.rating_books.users.infrastructure.persistence.PasswordResetTokenEntity;
import es.anusky.rating_books.users.infrastructure.persistence.SpringDataPasswordResetTokenRepository;
import es.anusky.rating_books.users.infrastructure.token.PasswordResetTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final SpringDataPasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenGenerator generator;
    private final UserMapper mapper;
    private final EmailService emailService;

    public void init(String alias) {
        User existing = userRepository.findByAlias(alias).orElse(null);
        if (existing == null) return;
        userRepository.update(existing.lock());
        String token = generator.generateNewToken(mapper.toEntity(existing));
        emailService.sendRecoverPasswordEmail(alias, existing.getEmail().getValue(), token);
        eventPublisher.publishEvent(buildAuditEvent(existing, "Reset password. Token: " + token));
    }

    public void reset(String token, String password) {
        PasswordResetTokenEntity existing = tokenRepository.findByToken(token).orElseThrow(
                () -> new TokenNotFoundException("Token not found")
        );
        if (existing.isUsed()) {
            throw new TokenAlreadyUsedException("Token already used");
        }
        if (existing.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired");
        }
        User user = userRepository.findById(existing.getUser().getId()).orElseThrow(
                () -> new UserNotFoundException("User not found while trying to active it!")
        );
        String encodedPassword = validateAndEncodePassword(password);
        var saved = userRepository.update(user.resetPassword(encodedPassword));
        eventPublisher.publishEvent(buildAuditEvent(saved, "Password reset successfully"));
        existing.setUsed(true);
        tokenRepository.save(existing);

    }

    private AuditEvent buildAuditEvent(User saved, String details) {
        return new AuditEvent(LocalDateTime.now(),
                Entities.USER.name(),
                saved.getUserId().getValue(),
                Actions.UPDATE.name(),
                saved.getAlias().getValue(),
                details);
    }

    private String validateAndEncodePassword(String password) {
        Password pass = new Password(password);
        return passwordEncoder.encode(pass.getValue());
    }
}
