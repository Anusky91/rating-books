package es.anusky.rating_books.users.infrastructure.token;

import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import es.anusky.rating_books.users.infrastructure.mapper.UserMapper;
import es.anusky.rating_books.users.infrastructure.persistence.PasswordResetTokenEntity;
import es.anusky.rating_books.users.infrastructure.persistence.SpringDataPasswordResetTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PasswordResetTokenGeneratorTest {

    PasswordResetTokenGenerator generator;
    SpringDataPasswordResetTokenRepository tokenRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        tokenRepository = mock(SpringDataPasswordResetTokenRepository.class);
        generator = new PasswordResetTokenGenerator(tokenRepository);
        userMapper = new UserMapper(passwordEncoder);
    }

    @Test
    void test_set_existing_token_as_used() {
        User user = UserMother.randomWithId(5L);
        PasswordResetTokenEntity existing = new PasswordResetTokenEntity(12L ,
                userMapper.toEntity(user),
                "4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e",
                now.minusHours(1),
                now.plusHours(23),
                false);


        PasswordResetTokenEntity newToken = PasswordResetTokenEntity.builder()
                .user(userMapper.toEntity(user))
                .token(UUID.randomUUID().toString())
                .createdAt(now)
                .expiresAt(now.plusHours(1L))
                .used(false).build();

        when(tokenRepository.findByUserAndUsedFalse(any())).thenReturn(Optional.of(existing));
        when(tokenRepository.save(any())).thenReturn(newToken);

        String generatedToken = generator.generateNewToken(userMapper.toEntity(user));

        verify(tokenRepository, times(2)).save(any());
        assertEquals(newToken.getToken(), generatedToken);
        assertNotEquals("4ebee2ce-f38a-48c2-ae6f-7c59aa9c826e", generatedToken);
    }

}