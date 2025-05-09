package es.anusky.rating_books.users.domain.valueobjects;

import com.github.javafaker.Faker;

public class PhoneNumberMother {

    private static final Faker faker = new Faker();

    public static PhoneNumber random() {
        String phoneNumber = faker.number().numberBetween(600_000_000, 799_999_999) + "";
        return new PhoneNumber(phoneNumber);
    }
}
