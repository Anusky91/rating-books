package es.anusky.rating_books.users.domain.model;

import com.github.javafaker.Faker;
import es.anusky.rating_books.shared.domain.valueobjects.Country;
import es.anusky.rating_books.users.domain.valueobjects.*;

import java.time.LocalDate;

public class UserMother {

    private static final Faker faker = new Faker();

    public static User random() {
        return User.create(new FirstName(faker.name().name()),
                new LastName(faker.name().lastName()),
                new Alias(faker.name().username()),
                EmailMother.random(),
                PhoneNumberMother.random(),
                PasswordMother.generate(),
                new Country("ES"),
                LocalDate.of(faker.number().numberBetween(1960, 2025), faker.number().numberBetween(1, 12), faker.number().numberBetween(1,28)),
                Role.USER,
                faker.chuckNorris().fact());
    }

    public static User withAdminRole() {
        return User.create(new FirstName(faker.name().name()),
                new LastName(faker.name().lastName()),
                new Alias("adminTest"),
                EmailMother.random(),
                PhoneNumberMother.random(),
                new Password("passwordTest8!"),
                new Country("ES"),
                LocalDate.of(1991, 4, 20),
                Role.ADMIN,
                "");
    }

}