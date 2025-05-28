package es.anusky.rating_books.ratings.application.getrating;

import es.anusky.rating_books.cqrs.application.query.Query;
import es.anusky.rating_books.shared.infrastructure.responses.RatingResponse;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class GetRatingByIdQuery implements Query<RatingResponse> {
    Long ratingId;
}
