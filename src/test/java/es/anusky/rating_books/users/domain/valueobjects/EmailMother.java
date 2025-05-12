package es.anusky.rating_books.users.domain.valueobjects;

import com.github.javafaker.Faker;

public class EmailMother {

    private static final Faker faker = new Faker();

    public static Email random() {
        String email = faker.name().username() + "@" + faker.funnyName().name() + ".com";
        String correctedEmail = email.replaceAll("\\s+", "").replaceAll("'", "");
        return new Email(correctedEmail);
    }

}