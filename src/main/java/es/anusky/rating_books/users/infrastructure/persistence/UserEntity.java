package es.anusky.rating_books.users.infrastructure.persistence;

import es.anusky.rating_books.users.domain.model.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String firstName;
    @Column(nullable = false, length = 200)
    private String lastName;
    @Column(nullable = false, unique = true, length = 50)
    private String alias;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private LocalDate birthDate;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private boolean enable;
    private boolean locked;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String avatarUrl;

    public UserEntity(Long id,
                      String firstName,
                      String lastName,
                      String alias,
                      String email,
                      String phoneNumber,
                      String password,
                      String country,
                      LocalDate birthDate,
                      boolean enable,
                      boolean locked,
                      Role role,
                      String avatarUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.country = country;
        this.birthDate = birthDate;
        this.enable = enable;
        this.locked = locked;
        this.role = role;
        this.avatarUrl = avatarUrl;
    }
}
