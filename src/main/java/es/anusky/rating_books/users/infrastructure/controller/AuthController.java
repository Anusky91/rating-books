package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.users.application.UserService;
import es.anusky.rating_books.users.domain.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody AuthRequest request) {
        User user = userService.findByAlias(request.alias()).orElseThrow(
                () -> new UserNotFoundException("User with alias " + request.alias() + " not found")
        );
        if (!passwordEncoder.matches(request.password, user.getPassword().getValue())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrecto");
        }

        if (user.isLocked() || !user.isEnable()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthStatusResponse(
                    user.getAlias().getValue(),
                    user.getRole().getAuthority(),
                    resolveStatus(user))
            );
        }

        return ResponseEntity.ok(new AuthStatusResponse(
                user.getAlias().getValue(),
                user.getRole().getAuthority(),
                resolveStatus(user))
        );
    }

    private String resolveStatus(User user) {
        if (!user.isEnable()) return "DISABLED";
        if (user.isLocked()) return "LOCKED";
        return "ACTIVE";
    }

    public record AuthRequest(String alias,
                              String password) {

    }

    public record AuthStatusResponse(String alias,
                                     String roles,
                                     String status) {

    }
}
