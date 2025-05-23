package es.anusky.rating_books.shared.infrastructure.responses;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.favorite.domain.model.Favorite;

public record FavoriteResponse(Long favId,
                               Long bookId,
                               String bookTitle,
                               String bookAuthor,
                               String addAt) {

    public static FavoriteResponse toResponse(Favorite favorite, Book book) {
        return new FavoriteResponse(favorite.getFavoriteId().getValue(),
                book.getId().getValue(),
                book.getTitle().getValue(),
                book.getAuthor().getValue(),
                favorite.getAddedAt().toString());
    }
}
