package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.shared.infrastructure.responses.UserAuthResponse;
import es.anusky.rating_books.users.application.auth.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserAuthController {

    private final AuthenticatedUserService service;

    @GetMapping("/me")
    public UserAuthResponse check() {
        return service.checkAuthenticatedUser();
    }

}
