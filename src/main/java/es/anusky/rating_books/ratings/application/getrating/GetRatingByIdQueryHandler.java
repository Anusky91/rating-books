package es.anusky.rating_books.ratings.application.getrating;

import es.anusky.rating_books.cqrs.application.query.QueryHandler;
import es.anusky.rating_books.ratings.application.RatingService;
import es.anusky.rating_books.shared.infrastructure.responses.RatingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRatingByIdQueryHandler implements QueryHandler<GetRatingByIdQuery, RatingResponse> {

    private final RatingService ratingService;

    @Override
    public RatingResponse handle(GetRatingByIdQuery query) {
        return RatingResponse.from(ratingService.findById(query.getRatingId()).orElseThrow());
    }
}
