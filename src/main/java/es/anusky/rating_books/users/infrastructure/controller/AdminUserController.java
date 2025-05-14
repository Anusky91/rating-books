package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.shared.infrastructure.responses.UserResponse;
import es.anusky.rating_books.users.application.UserService;
import es.anusky.rating_books.users.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll()
                .stream()
                .map(UserResponse::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with ID " + id + " not found")
        );
        return UserResponse.toResponse(user);
    }

    @GetMapping("/email/{email}")
    public UserResponse findByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email " + email + " not found")
        );
        return UserResponse.toResponse(user);
    }

    @GetMapping("/alias/{alias}")
    public UserResponse findByAlias(@PathVariable String alias) {
        User user = userService.findByAlias(alias).orElseThrow(
                () -> new UserNotFoundException("User with alias " + alias + " not found")
        );
        return UserResponse.toResponse(user);
    }
}
