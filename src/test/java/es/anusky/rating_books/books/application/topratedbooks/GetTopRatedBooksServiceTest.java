package es.anusky.rating_books.books.application.topratedbooks;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.books.domain.model.BookWithRating;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.books.infrastructure.persistence.BookRepositoryImpl;
import es.anusky.rating_books.ratings.domain.model.RatingMother;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import es.anusky.rating_books.ratings.infrastructure.persistence.RatingRepositoryImpl;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetTopRatedBooksServiceTest {

    GetTopRatedBooksService service;
    BookRepository bookRepository;
    RatingRepository ratingRepository;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepositoryImpl.class);
        ratingRepository = mock(RatingRepositoryImpl.class);
        service = new GetTopRatedBooksService(bookRepository, ratingRepository);
    }

    @Test
    void test_execute() {
        Book book1 = BookMother.randomWithId(1L);
        Book book2 = BookMother.randomWithId(2L);
        Book book3 = BookMother.randomWithId(3L);
        Book book4 = BookMother.randomWithId(4L);
        Book book5 = BookMother.randomWithId(5L);
        Book book6 = BookMother.randomWithId(6L);
        User user1 = UserMother.randomWithId(1L);
        User user2 = UserMother.randomWithId(2L);
        User user3 = UserMother.randomWithId(3L);

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2, book3, book4, book5, book6));
        when(ratingRepository.findAll()).thenReturn(List.of(RatingMother.with(book1, user1),
                                                    RatingMother.with(book2, user1),
                                                    RatingMother.with(book3, user1),
                                                    RatingMother.with(book1, user2),
                                                    RatingMother.with(book2, user2),
                                                    RatingMother.with(book3, user2),
                                                    RatingMother.with(book1, user3),
                                                    RatingMother.with(book2, user3),
                                                    RatingMother.with(book3, user3),
                                                    RatingMother.with(book4, user1),
                                                    RatingMother.with(book5, user1),
                                                    RatingMother.with(book6, user1)));

        List<BookWithRating> ratedBooks = service.execute(5);
        assertEquals(5, ratedBooks.size());
        assertTrue(
                ratedBooks.stream().allMatch(book -> book.getAvgScore() >= 1 && book.getAvgScore() <= 5),
                "All average scores should be between 1 and 5"
        );

    }

}