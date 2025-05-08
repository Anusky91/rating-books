package es.anusky.rating_books.infrastructure.exception;

public class RatingNotFoundException extends RuntimeException{
    public RatingNotFoundException(String message) {
        super(message);
    }
}
