package es.anusky.rating_books.books.domain.model;

import com.github.javafaker.Faker;
import es.anusky.rating_books.books.domain.valueobjects.*;

public class BookMother {

    private static final Faker faker = new Faker();

    public static Book random() {
        return new Book(null,
                new Title(faker.book().title()),
                new Author(faker.book().author()),
                new Editorial(faker.book().publisher()),
                IsbnMother.random());
    }

    public static Book withId(Long id) {
        return new Book(null,
                new Title(faker.book().title()),
                new Author(faker.book().author()),
                new Editorial(faker.book().publisher()),
                IsbnMother.random());
    }
}
