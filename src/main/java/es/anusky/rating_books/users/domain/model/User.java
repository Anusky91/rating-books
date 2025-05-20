package es.anusky.rating_books.users.domain.model;

import es.anusky.rating_books.shared.domain.valueobjects.Country;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import es.anusky.rating_books.users.domain.valueobjects.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class User {

    private final UserId userId;
    private final FirstName firstName;
    private final LastName lastName;
    private final Alias alias;
    private final Email email;
    private final PhoneNumber phoneNumber;
    private final Password password;
    private final Country country;
    private final LocalDate birthDate;
    private final LocalDateTime createdAt;
    private final boolean enable;
    private final boolean locked;
    private final Role role;
    private final String avatarUrl;

    /**
     * Crea una nueva instancia de usuario con estado inicial bloqueado y no habilitado.
     * Esto representa un flujo típico de registro donde el usuario:
     * - Aún no ha sido activado (por ejemplo, debe confirmar su correo)
     * - No puede iniciar sesión hasta que su cuenta sea habilitada por el sistema o un flujo de verificación
     * Por defecto:
     * - enable = false → el usuario aún no está activo
     * - locked = true → el usuario no puede autenticarse hasta ser desbloqueado
     */
    public static User create(FirstName firstName,
                              LastName lastName,
                              Alias alias,
                              Email email,
                              PhoneNumber phoneNumber,
                              Password password,
                              Country country,
                              LocalDate birthDate,
                              Role role,
                              String avatarUrl) {
        return new User(null,
                firstName,
                lastName,
                alias,
                email,
                phoneNumber,
                password,
                country,
                birthDate,
                null,
                false,
                true,
                role,
                avatarUrl);
    }

    public User activate() {
        return new User(
                this.userId,
                this.firstName,
                this.lastName,
                this.alias,
                this.email,
                this.phoneNumber,
                this.password,
                this.country,
                this.birthDate,
                this.createdAt,
                true,
                false,
                this.role,
                this.avatarUrl
        );
    }

}
