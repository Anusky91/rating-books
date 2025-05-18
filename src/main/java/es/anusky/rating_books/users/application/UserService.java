package es.anusky.rating_books.users.application;

import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.shared.domain.valueobjects.Country;
import es.anusky.rating_books.users.domain.model.Role;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import es.anusky.rating_books.users.domain.valueobjects.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public User create(String firstName,
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

        var saved = userRepository.save(newUser);
        publishEvent(saved);
        return saved;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByAlias(String alias) {
        return userRepository.findByAlias(alias);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void publishEvent(User saved) {
        eventPublisher.publishEvent(new AuditEvent(LocalDateTime.now(),
                Entities.USER.name(),
                saved.getUserId().getValue(),
                Actions.CREATE.name(),
                saved.getAlias().getValue(),
                ""));
    }
}
