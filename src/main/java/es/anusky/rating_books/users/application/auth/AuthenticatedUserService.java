package es.anusky.rating_books.users.application.auth;

import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.shared.infrastructure.responses.UserAuthResponse;
import es.anusky.rating_books.shared.infrastructure.security.AuthenticatedUserProvider;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticatedUserService {

    private final AuthenticatedUserProvider userProvider;
    private final UserRepository userRepository;

    public UserAuthResponse checkAuthenticatedUser() {
        String alias = userProvider.getCurrentAlias();
        User user = userRepository.findByAlias(alias).orElseThrow(
                () -> new UserNotFoundException("User with alias " + alias + " not found")
        );

        log.info("User with alias {} authenticated", alias);

        return UserAuthResponse.builder()
                .id(user.getUserId().getValue())
                .alias(user.getAlias().getValue())
                .email(user.getEmail().getValue())
                .role(user.getRole().toString())
                .enable(user.isEnable())
                .locked(user.isLocked())
                .build();
    }
}
