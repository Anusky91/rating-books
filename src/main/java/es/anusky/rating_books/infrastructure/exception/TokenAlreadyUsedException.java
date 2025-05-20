package es.anusky.rating_books.infrastructure.exception;

public class TokenAlreadyUsedException extends RuntimeException{
    public TokenAlreadyUsedException(String message) {
        super(message);
    }
}
