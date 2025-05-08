package es.anusky.rating_books.shared.infrastructure.responses;

import es.anusky.rating_books.ratings.domain.model.Rating;

import java.time.LocalDate;

public record RatingResponse(Long id,
                             Long bookId,
                             Long userId,
                             int score,
                             String comment,
                             LocalDate date) {

    public static RatingResponse from(Rating rating) {
        return new RatingResponse(
                rating.getId().getValue(),
                rating.getBookId(),
                rating.getUserId().getValue(),
                rating.getScore().getValue(),
                rating.getComment().getValue(),
                rating.getDate()
        );
    }
}
