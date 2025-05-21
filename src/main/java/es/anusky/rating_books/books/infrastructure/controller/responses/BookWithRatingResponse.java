package es.anusky.rating_books.books.infrastructure.controller.responses;

import es.anusky.rating_books.books.domain.model.BookWithRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookWithRatingResponse {
    private Long bookId;
    private String title;
    private String author;
    private Double averageRating;

    public static BookWithRatingResponse from(BookWithRating book) {
        return BookWithRatingResponse.builder()
                .bookId(book.getBookId().getValue())
                .title(book.getTitle().getValue())
                .author(book.getAuthor().getValue())
                .averageRating(book.getAvgScore())
                .build();
    }
}
