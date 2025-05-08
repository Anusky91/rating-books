package es.anusky.rating_books.shared.infrastructure.responses;

import es.anusky.rating_books.books.domain.model.Book;

public record BookResponse(Long id,
                           String title,
                           String author,
                           String editorial,
                           String isbn) {

    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId().getValue(),
                book.getTitle().getValue(),
                book.getAuthor().getValue(),
                book.getEditorial().getValue(),
                book.getIsbn().getValue()
        );
    }
}
