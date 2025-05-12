package es.anusky.rating_books.infrastructure.exception;

public class BookAlreadyRatedByUserException extends RuntimeException{
    public BookAlreadyRatedByUserException(String message) {
        super(message);
    }
}
