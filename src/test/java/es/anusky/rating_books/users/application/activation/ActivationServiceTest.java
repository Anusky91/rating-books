package es.anusky.rating_books.users.application.activation;

import es.anusky.rating_books.infrastructure.exception.TokenAlreadyUsedException;
import es.anusky.rating_books.infrastructure.exception.TokenExpiredException;
import es.anusky.rating_books.infrastructure.exception.TokenNotFoundException;
import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import es.anusky.rating_books.users.infrastructure.mapper.UserMapper;
import es.anusky.rating_books.users.infrastructure.persistence.ActivationTokenEntity;
import es.anusky.rating_books.users.infrastructure.persistence.SpringDataActivationTokenRepository;
import es.anusky.rating_books.users.infrastructure.persistence.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActivationServiceTest {

    ActivationService activationService;
    SpringDataActivationTokenRepository tokenRepository;
    UserRepository userRepository;
    ApplicationEventPublisher eventPublisher;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        tokenRepository = mock(SpringDataActivationTokenRepository.class);
        userRepository = mock(UserRepositoryImpl.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        userMapper = new UserMapper(passwordEncoder);
        activationService = new ActivationService(tokenRepository, userRepository, eventPublisher);
    }

    @Test
    void test_throws_TokenNotFoundException() {
        String token = UUID.randomUUID().toString();

        assertThrows(TokenNotFoundException.class, () -> activationService.activate(token));
    }

    @Test
    void test_throws_TokenAlreadyUsedException() {
        String token = "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e";
        User user = UserMother.randomWithId(5L);
        ActivationTokenEntity existing = new ActivationTokenEntity(12L ,
                userMapper.toEntity(user),
                "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(23),
                true);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(existing));

        assertThrows(TokenAlreadyUsedException.class, () -> activationService.activate(token));
    }

    @Test
    void test_throws_TokenExpiredException() {
        String token = "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e";
        User user = UserMother.randomWithId(5L);
        ActivationTokenEntity existing = new ActivationTokenEntity(12L ,
                userMapper.toEntity(user),
                "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e",
                LocalDateTime.now().minusHours(25),
                LocalDateTime.now().minusHours(1),
                false);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(existing));

        assertThrows(TokenExpiredException.class, () -> activationService.activate(token));
    }

    @Test
    void test_throws_UserNotFoundException() {
        String token = "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e";
        User user = UserMother.randomWithId(5L);
        ActivationTokenEntity existing = new ActivationTokenEntity(12L ,
                userMapper.toEntity(user),
                "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(23),
                false);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(existing));

        assertThrows(UserNotFoundException.class, () -> activationService.activate(token));
    }

    @Test
    void test_activate_user() {
        String token = "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e";
        User user = UserMother.randomWithId(5L);
        ActivationTokenEntity existing = new ActivationTokenEntity(12L ,
                userMapper.toEntity(user),
                "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(23),
                false);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(existing));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.update(any())).thenReturn(user.activate());

        assertDoesNotThrow(() -> activationService.activate(token));
        assertTrue(existing.isUsed());
    }

}