package es.anusky.rating_books.users.domain.valueobjects;

import com.github.javafaker.Faker;

public class EmailMother {

    private static final Faker faker = new Faker();

    public static Email random() {
        String email = faker.name().username() + "@" + faker.pokemon().name() + ".com";
        String emailSinEspacios = email.replaceAll("\\s+", "");
        return new Email(emailSinEspacios);
    }

}