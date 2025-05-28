package es.anusky.rating_books.ratings.application.getrating;

import es.anusky.rating_books.cqrs.application.query.QueryHandler;
import es.anusky.rating_books.ratings.application.RatingService;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.shared.infrastructure.responses.RatingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserRatingForBookQueryHandler implements QueryHandler<GetUserRatingForBookQuery, RatingResponse> {

    private final RatingService ratingService;

    @Override
    public RatingResponse handle(GetUserRatingForBookQuery query) {
        Rating saved = ratingService.findByUserIdAndBookId(query.getBookId()).orElseThrow();
        return RatingResponse.from(saved);
    }
}
