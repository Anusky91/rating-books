package es.anusky.rating_books.favorite.domain.model;

import es.anusky.rating_books.books.domain.valueobjects.BookId;
import es.anusky.rating_books.favorite.domain.valueobjects.FavoriteId;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Favorite {

    private final FavoriteId favoriteId;
    private final BookId bookId;
    private final UserId userId;
    private final LocalDateTime addedAt;

    public static Favorite create(FavoriteId id, BookId bookId, UserId userId) {
        return new Favorite(id, bookId, userId, LocalDateTime.now());
    }
}
