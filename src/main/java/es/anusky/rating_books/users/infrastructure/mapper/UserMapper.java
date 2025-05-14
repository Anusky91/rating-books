package es.anusky.rating_books.users.infrastructure.mapper;

import es.anusky.rating_books.shared.domain.valueobjects.Country;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.valueobjects.*;
import es.anusky.rating_books.users.infrastructure.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder encoder;

    public User toDomain(UserEntity entity) {
        return new User(new UserId(entity.getId()),
                new FirstName(entity.getFirstName()),
                new LastName(entity.getLastName()),
                new Alias(entity.getAlias()),
                new Email(entity.getEmail()),
                new PhoneNumber(entity.getPhoneNumber()),
                Password.fromEncoded(entity.getPassword()),
                new Country(entity.getCountry()),
                entity.getBirthDate(),
                entity.isEnable(),
                entity.isLocked(),
                entity.getRole(),
                entity.getAvatarUrl());
    }

    public UserEntity toEntity(User user) {
        if (user.getUserId() != null) {
            return new UserEntity(user.getUserId().getValue(),
                    user.getFirstName().getValue(),
                    user.getLastName().getValue(),
                    user.getAlias().getValue(),
                    user.getEmail().getValue(),
                    user.getPhoneNumber().getValue(),
                    user.getPassword().getValue(),
                    user.getCountry().getCode(),
                    user.getBirthDate(),
                    user.isEnable(),
                    user.isLocked(),
                    user.getRole(),
                    user.getAvatarUrl());
        }

        return new UserEntity(null,
                user.getFirstName().getValue(),
                user.getLastName().getValue(),
                user.getAlias().getValue(),
                user.getEmail().getValue(),
                user.getPhoneNumber().getValue(),
                encoder.encode(user.getPassword().getValue()),
                user.getCountry().getCode(),
                user.getBirthDate(),
                user.isEnable(),
                user.isLocked(),
                user.getRole(),
                user.getAvatarUrl());
    }
}
