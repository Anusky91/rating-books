package es.anusky.rating_books.books.domain.model;

import es.anusky.rating_books.books.domain.valueobjects.*;

import java.util.List;

public record BookDetails (
    BookId bookId,
    Title title,
    Author author,
    Editorial editorial,
    Isbn isbn,
    Double avgScore,
    int totalRatings,
    List<String> lastComments
){
}
