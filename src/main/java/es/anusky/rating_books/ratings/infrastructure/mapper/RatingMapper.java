package es.anusky.rating_books.ratings.infrastructure.mapper;

import es.anusky.rating_books.books.infrastucture.persistence.BookEntity;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingComment;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingId;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingScore;
import es.anusky.rating_books.ratings.domain.valueobjects.UserId;
import es.anusky.rating_books.ratings.infrastructure.persistence.RatingEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RatingMapper {

    public RatingEntity toEntity(Rating rating, BookEntity bookEntity) {
        return RatingEntity.builder()
                .id(rating.getId() != null ? rating.getId().getValue() : null)
                .book(bookEntity)
                .userId(rating.getUserId().getValue())
                .score(rating.getScore().getValue())
                .comment(rating.getComment().getValue())
                .date(rating.getDate())
                .build();
    }

    public Rating toDomain(RatingEntity entity) {
        return new Rating(new RatingId(entity.getId()),
                entity.getBook().getId(),
                new UserId(entity.getUserId()),
                new RatingScore(entity.getScore()),
                new RatingComment(entity.getComment()),
                entity.getDate());
    }
}
