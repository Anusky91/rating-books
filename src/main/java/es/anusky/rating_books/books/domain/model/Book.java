package es.anusky.rating_books.books.domain.model;

import es.anusky.rating_books.books.domain.valueobjects.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Book {

    private final BookId id;
    private final Title title;
    private final Author author;
    private final Editorial editorial;
    private final Isbn isbn;

    public static Book create(Title title, Author author, Editorial editorial, Isbn isbn) {
        return new Book(null, title, author, editorial, isbn);
    }

}
