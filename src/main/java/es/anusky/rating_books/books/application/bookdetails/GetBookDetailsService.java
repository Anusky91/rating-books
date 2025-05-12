package es.anusky.rating_books.books.application.bookdetails;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookDetails;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBookDetailsService {

    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;

    public BookDetails getDetails(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new BookNotFoundException("Book with ID " + bookId + " not found")
        );
        List<Rating> ratings = ratingRepository.findByBookId(bookId);
        if (ratings.isEmpty()) {
            return new BookDetails(book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getEditorial(),
                    book.getIsbn(),
                    0D,
                    0,
                    List.of());
        }
        List<String> lastComments = ratings.stream().sorted(Comparator.comparing(Rating::getDate).reversed())
                                        .map(rating -> rating.getComment().getValue())
                                        .limit(10)
                                        .toList();
        int sumScore = ratings.stream().map(r -> r.getScore().getValue()).reduce(0, Integer::sum);
        int totalRatings = ratings.size();
        double avgScore = (double) sumScore / totalRatings;
        return new BookDetails(book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getEditorial(),
                book.getIsbn(),
                avgScore,
                totalRatings,
                lastComments);
    }
}
