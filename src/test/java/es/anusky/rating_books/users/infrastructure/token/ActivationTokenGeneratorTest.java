package es.anusky.rating_books.users.infrastructure.token;

import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import es.anusky.rating_books.users.infrastructure.mapper.UserMapper;
import es.anusky.rating_books.users.infrastructure.persistence.ActivationTokenEntity;
import es.anusky.rating_books.users.infrastructure.persistence.SpringDataActivationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ActivationTokenGeneratorTest {

    ActivationTokenGenerator generator;
    SpringDataActivationTokenRepository tokenRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        tokenRepository = mock(SpringDataActivationTokenRepository.class);
        generator = new ActivationTokenGenerator(tokenRepository);
        userMapper = new UserMapper(passwordEncoder);
    }

    @Test
    void test_sets_token_as_used() {
        User user = UserMother.randomWithId(5L);
        ActivationTokenEntity existing = new ActivationTokenEntity(12L ,
                userMapper.toEntity(user),
                "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(23),
                false);

        when(tokenRepository.findByUserAndUsedFalse(any())).thenReturn(Optional.of(existing));
        when(tokenRepository.save(any())).thenReturn(existing);

        String generatedToken = generator.generateNewToken(userMapper.toEntity(user));

        verify(tokenRepository, times(2)).save(any());
        assertEquals("4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e", generatedToken);
    }

    @Test
    void test_create_new_token() {
        User user = UserMother.randomWithId(5L);
        ActivationTokenEntity existing = new ActivationTokenEntity(12L ,
                userMapper.toEntity(user),
                "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(23),
                false);

        when(tokenRepository.findByUserAndUsedFalse(any())).thenReturn(Optional.empty());
        when(tokenRepository.save(any())).thenReturn(existing);

        String generatedToken = generator.generateNewToken(userMapper.toEntity(user));

        verify(tokenRepository, times(1)).save(any());
        assertFalse(existing.isUsed());
        assertEquals("4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e", generatedToken);
    }

}