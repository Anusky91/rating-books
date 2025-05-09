package es.anusky.rating_books.users.infrastructure.mapper;

import es.anusky.rating_books.shared.domain.valueobjects.Country;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.valueobjects.*;
import es.anusky.rating_books.users.infrastructure.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        return new User(new UserId(entity.getId()),
                new FirstName(entity.getFirstName()),
                new LastName(entity.getLastName()),
                new Alias(entity.getAlias()),
                new Email(entity.getEmail()),
                new PhoneNumber(entity.getPhoneNumber()),
                new Password(entity.getPassword()),
                new Country(entity.getCountry()),
                entity.getBirthDate(),
                entity.isEnable(),
                entity.isLocked(),
                entity.getRole(),
                entity.getAvatarUrl());
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(user.getUserId() != null ? user.getUserId().getValue() : null,
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
}
