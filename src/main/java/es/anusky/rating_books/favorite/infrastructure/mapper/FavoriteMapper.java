package es.anusky.rating_books.favorite.infrastructure.mapper;

import es.anusky.rating_books.books.domain.valueobjects.BookId;
import es.anusky.rating_books.books.infrastructure.persistence.BookEntity;
import es.anusky.rating_books.favorite.domain.model.Favorite;
import es.anusky.rating_books.favorite.domain.valueobjects.FavoriteId;
import es.anusky.rating_books.favorite.infrastructure.persistence.FavoriteEntity;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import es.anusky.rating_books.users.infrastructure.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    public Favorite toDomain(FavoriteEntity entity) {
        return new Favorite(new FavoriteId(entity.getId()),
                new BookId(entity.getBook().getId()),
                new UserId(entity.getUser().getId()),
                entity.getAddedAt());
    }

    public FavoriteEntity toEntity(Favorite favorite, BookEntity book, UserEntity user) {
        return FavoriteEntity.builder()
                .id(favorite.getFavoriteId() != null ? favorite.getFavoriteId().getValue() : null)
                .book(book)
                .user(user)
                .addedAt(favorite.getAddedAt()).build();
    }
}
