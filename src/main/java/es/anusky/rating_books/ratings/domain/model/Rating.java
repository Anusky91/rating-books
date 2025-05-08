package es.anusky.rating_books.ratings.domain.model;

import es.anusky.rating_books.ratings.domain.valueobjects.RatingComment;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingId;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingScore;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Rating {

    private final RatingId id;
    private final Long bookId;
    private final UserId userId;
    private final RatingScore score;
    private final RatingComment comment;
    private final LocalDate date;

    public static Rating create(Long bookId,
                                UserId userId,
                                RatingScore score,
                                RatingComment comment,
                                LocalDate date) {
        return new Rating(null, bookId, userId, score, comment, date);
    }
}
