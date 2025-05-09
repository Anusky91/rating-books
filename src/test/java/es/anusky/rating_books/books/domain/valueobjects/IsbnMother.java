package es.anusky.rating_books.books.domain.valueobjects;

import com.github.javafaker.Faker;

public class IsbnMother {

    public static Isbn random() {
        return new Isbn(generateValidIsbn());
    }

    private static String generateValidIsbn() {
        String group = String.format("%01d", Faker.instance().number().numberBetween(0, 9));
        String registrant = String.format("%02d", Faker.instance().number().numberBetween(0, 99));
        String publication = String.format("%06d", Faker.instance().number().numberBetween(0, 999999));
        String checkDigit = String.format("%01d", Faker.instance().number().numberBetween(0, 9));

        // Result: 978-1-23456-78901-2 → normaliza a 13 dígitos
        return String.format("978-%s-%s-%s-%s", group, registrant, publication, checkDigit);
    }

}
