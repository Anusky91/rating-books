package es.anusky.rating_books.users.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataPasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    Optional<PasswordResetTokenEntity> findByUserAndUsedFalse(UserEntity user);
    Optional<PasswordResetTokenEntity> findByToken(String token);
}
