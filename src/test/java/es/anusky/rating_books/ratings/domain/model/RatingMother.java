package es.anusky.rating_books.ratings.domain.model;

import com.github.javafaker.Faker;
import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingComment;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingScore;
import es.anusky.rating_books.users.domain.model.User;

import java.time.LocalDate;

public class RatingMother {

    private static final Faker faker = new Faker();

    public static Rating with(Book book, User user) {
        return Rating.create(book.getId().getValue(),
                user.getUserId(),
                new RatingScore(faker.number().numberBetween(1, 5)),
                new RatingComment(faker.shakespeare().hamletQuote()),
                LocalDate.now());
    }

}