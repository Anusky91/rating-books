package es.anusky.rating_books.books.domain.model;

import com.github.javafaker.Faker;
import es.anusky.rating_books.books.domain.valueobjects.*;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class BookMother {

    private static final Faker faker = new Faker();

    public static Book random() {
        return new Book(null,
                new Title(faker.book().title()),
                new Author(faker.book().author()),
                new Editorial(faker.book().publisher()),
                IsbnMother.random(),
                between(LocalDate.of(1800, 1,1), LocalDate.now()));
    }

    public static Book randomWithId(Long id) {
        return new Book(new BookId(id),
                new Title(faker.book().title()),
                new Author(faker.book().author()),
                new Editorial(faker.book().publisher()),
                IsbnMother.random(),
                between(LocalDate.of(1800, 1,1), LocalDate.now()));
    }

    private static LocalDate between(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }

}
