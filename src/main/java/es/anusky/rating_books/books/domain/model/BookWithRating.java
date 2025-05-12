package es.anusky.rating_books.books.domain.model;

import es.anusky.rating_books.books.domain.valueobjects.Author;
import es.anusky.rating_books.books.domain.valueobjects.BookId;
import es.anusky.rating_books.books.domain.valueobjects.Title;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class BookWithRating {
    private BookId bookId;
    private Title title;
    private Author author;
    private Double avgScore;
}
