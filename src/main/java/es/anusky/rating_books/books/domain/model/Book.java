package es.anusky.rating_books.books.domain.model;

import es.anusky.rating_books.books.domain.valueobjects.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Book {

    private final BookId id;
    private final Title title;
    private final Author author;
    private final Editorial editorial;
    private final Isbn isbn;
    private final LocalDate publicationDate;

    public static Book create(Title title, Author author, Editorial editorial, Isbn isbn, LocalDate publicationDate) {
        return new Book(null, title, author, editorial, isbn, publicationDate);
    }

}
