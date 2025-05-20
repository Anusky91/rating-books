package es.anusky.rating_books.users.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataActivationTokenRepository extends JpaRepository<ActivationTokenEntity, Long> {
    Optional<ActivationTokenEntity> findByUserAndUsedFalse(UserEntity user);
    Optional<ActivationTokenEntity> findByToken(String token);
}
