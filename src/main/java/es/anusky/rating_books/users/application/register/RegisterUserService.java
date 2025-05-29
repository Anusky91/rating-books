package es.anusky.rating_books.users.application.register;

import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.shared.domain.event.AuditEventFactory;
import es.anusky.rating_books.shared.domain.valueobjects.Country;
import es.anusky.rating_books.users.domain.model.Role;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import es.anusky.rating_books.users.domain.valueobjects.*;
import es.anusky.rating_books.users.infrastructure.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RegisterUserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final EmailService emailService;
    private final AuditEventFactory eventFactory;

    public void register(String firstName,
                         String lastName,
                         String alias,
                         String email,
                         String phoneNumber,
                         String password,
                         String country,
                         String birthDate,
                         String role,
                         String avatarUrl) {
        User newUser = User.create(new FirstName(firstName),
                new LastName(lastName),
                new Alias(alias),
                new Email(email),
                new PhoneNumber(phoneNumber),
                new Password(password),
                new Country(country),
                LocalDate.parse(birthDate),
                Role.valueOf(role),
                avatarUrl);

        var saved = userRepository.create(newUser);
        emailService.sendActivationEmail(saved.getFirst().getEmail().getValue(), saved.getSecond());
        eventPublisher.publishEvent(buildAuditEvent(saved.getFirst(), saved.getSecond()));
    }

    private AuditEvent buildAuditEvent(User saved, String token) {
        return eventFactory.create(
                Entities.USER,
                saved.getUserId().getValue(),
                Actions.CREATE,
                saved.getAlias().getValue(),
                "Token: " + token);
    }
}
