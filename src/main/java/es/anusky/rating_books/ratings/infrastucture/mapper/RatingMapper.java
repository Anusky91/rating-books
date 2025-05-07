package es.anusky.rating_books.ratings.infrastucture.mapper;

import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingComment;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingId;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingScore;
import es.anusky.rating_books.ratings.domain.valueobjects.UserId;
import es.anusky.rating_books.ratings.infrastucture.persistence.RatingEntity;

public class RatingMapper {
    public static RatingEntity toEntity(Rating rating) {
        return RatingEntity.builder()
                .id(rating.getId() != null ? rating.getId().getValue() : null)
                .bookId(rating.getBookId())
                .userId(rating.getUserId().getValue())
                .score(rating.getScore().getValue())
                .comment(rating.getComment().getValue())
                .date(rating.getDate())
                .build();
    }

    public static Rating toDomain(RatingEntity entity) {
        return new Rating(new RatingId(entity.getId()),
                entity.getBookId(),
                new UserId(entity.getUserId()),
                new RatingScore(entity.getScore()),
                new RatingComment(entity.getComment()),
                entity.getDate());
    }
}
