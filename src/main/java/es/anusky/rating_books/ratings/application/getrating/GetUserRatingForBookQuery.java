package es.anusky.rating_books.ratings.application.getrating;

import es.anusky.rating_books.cqrs.application.query.Query;
import es.anusky.rating_books.shared.infrastructure.responses.RatingResponse;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class GetUserRatingForBookQuery implements Query<RatingResponse> {
    Long bookId;

    public GetUserRatingForBookQuery(Long bookId) {
        super();
        this.bookId = bookId;
    }
}
