package es.anusky.rating_books.infrastructure.exception;

public class FavoriteAlreadyExistsException extends RuntimeException{
    public FavoriteAlreadyExistsException(Long bookId, Long userId) {
        super("Favorite with BookId" + bookId + " and UserId " + userId + "already exists");
    }
}
