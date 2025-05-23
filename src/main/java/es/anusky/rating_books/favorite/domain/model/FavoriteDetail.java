package es.anusky.rating_books.favorite.domain.model;

import es.anusky.rating_books.books.domain.valueobjects.BookId;
import es.anusky.rating_books.books.domain.valueobjects.Title;
import es.anusky.rating_books.favorite.domain.valueobjects.FavoriteId;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingComment;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingScore;

public record FavoriteDetail(
    FavoriteId favoriteId,
    BookId bookId,
    Title title,
    RatingScore score,
    RatingComment comment ){

    public static FavoriteDetail withoutRating(FavoriteId favoriteId,  BookId bookId, Title title) {
        return new FavoriteDetail(favoriteId, bookId, title, null, null);
    }
}
