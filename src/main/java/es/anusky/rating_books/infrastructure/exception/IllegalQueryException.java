package es.anusky.rating_books.infrastructure.exception;

public class IllegalQueryException extends RuntimeException{
    public IllegalQueryException(String message) {
        super(message);
    }
}
