package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.shared.infrastructure.responses.UserPublicProfileResponse;
import es.anusky.rating_books.users.application.publicprofile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/me/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService service;

    @GetMapping
    public UserPublicProfileResponse getProfile() {
        return service.getPublicProfile();
    }
}
