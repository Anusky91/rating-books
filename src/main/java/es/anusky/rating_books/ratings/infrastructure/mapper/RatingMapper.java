package es.anusky.rating_books.ratings.infrastructure.mapper;

import es.anusky.rating_books.books.infrastructure.persistence.BookEntity;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingComment;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingId;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingScore;
import es.anusky.rating_books.ratings.infrastructure.persistence.RatingEntity;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import es.anusky.rating_books.users.infrastructure.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    public RatingEntity toEntity(Rating rating, BookEntity bookEntity, UserEntity userEntity) {
        return RatingEntity.builder()
                .id(rating.getId() != null ? rating.getId().getValue() : null)
                .book(bookEntity)
                .user(userEntity)
                .score(rating.getScore().getValue())
                .comment(rating.getComment().getValue())
                .date(rating.getDate())
                .build();
    }

    public Rating toDomain(RatingEntity entity) {
        return new Rating(new RatingId(entity.getId()),
                entity.getBook().getId(),
                new UserId(entity.getUser().getId()),
                new RatingScore(entity.getScore()),
                new RatingComment(entity.getComment()),
                entity.getDate());
    }
}
