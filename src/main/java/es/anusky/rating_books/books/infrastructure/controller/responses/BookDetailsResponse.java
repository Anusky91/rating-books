package es.anusky.rating_books.books.infrastructure.controller.responses;

import es.anusky.rating_books.books.domain.model.BookDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDetailsResponse {
    private Long bookId;
    private String title;
    private String author;
    private String editorial;
    private String isbn;
    private String publicationDate;
    private Double avgScore;
    private int totalRatings;
    private List<String> lastComments;

    public static BookDetailsResponse from(BookDetails bookDetails) {
        return BookDetailsResponse.builder()
                .bookId(bookDetails.bookId().getValue())
                .title(bookDetails.title().getValue())
                .author(bookDetails.author().getValue())
                .editorial(bookDetails.editorial().getValue())
                .isbn(bookDetails.isbn().getValue())
                .publicationDate(bookDetails.publicationDate().toString())
                .avgScore(bookDetails.avgScore())
                .totalRatings(bookDetails.totalRatings())
                .lastComments(bookDetails.lastComments())
                .build();
    }
}
