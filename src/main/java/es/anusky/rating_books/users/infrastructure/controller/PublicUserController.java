package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.shared.infrastructure.responses.UserResponse;
import es.anusky.rating_books.users.application.UserService;
import es.anusky.rating_books.users.domain.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users")
@RestController
@RequestMapping("/public/users")
@RequiredArgsConstructor
public class PublicUserController {

    private final UserService userService;

    @PostMapping
    public UserResponse create(@RequestBody @Valid CreateUserRequest request) {
        User user = userService.create(request.firstName(),
                request.lastName(),
                request.alias(),
                request.email(),
                request.phoneNumber(),
                request.password(),
                request.country(),
                request.birthDate(),
                request.role(),
                request.avatarUrl());
        return new UserResponse(user.getUserId().getValue(),
                user.getAlias().getValue(),
                user.getEmail().getValue(),
                user.getRole().toString());
    }

    public record CreateUserRequest(@NotBlank(message = "El nombre es obligatorio")
                                    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
                                    String firstName,
                                    @NotBlank(message = "Los apellidos son obligatorios")
                                    @Size(max = 200, message = "Los apellidos no pueden tener más de 200 caracteres")
                                    String lastName,
                                    @NotBlank(message = "El alias es obligatorio")
                                    @Size(min = 3, max = 50, message = "El alias debe tener entre 3 y 50 caracteres")
                                    String alias,
                                    @NotBlank(message = "El email es obligatorio")
                                    @Email(message = "El email tiene un formato inválido")
                                    String email,
                                    @NotBlank(message = "El número de teléfono es obligatorio")
                                    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener exactamente 9 dígitos")
                                    String phoneNumber,
                                    @NotBlank(message = "La contraseña es obligatoria")
                                    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
                                    String password,
                                    @NotBlank(message = "El país es obligatorio")
                                    String country,
                                    @NotBlank(message = "La fecha de nacimiento es obligatoria")
                                    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "La fecha debe estar en formato YYYY-MM-DD")
                                    String birthDate,
                                    @NotBlank(message = "El rol es obligatorio")
                                    String role,
                                    String avatarUrl){
    }
}
