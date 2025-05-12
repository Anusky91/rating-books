package es.anusky.rating_books.books.application.topratedbooks;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookWithRating;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class GetTopRatedBooksService {

    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;

    public List<BookWithRating> execute(int limit) {

        List<Book> books = bookRepository.findAll();
        List<Rating> ratings = ratingRepository.findAll();

        List<BookWithRating> bookWithRatingList = new ArrayList<>();

        for (Book book: books) {
            AtomicInteger numberOfRatings = new AtomicInteger();
            AtomicInteger sum = new AtomicInteger();
            ratings.stream().filter(rating -> rating.getBookId().equals(book.getId().getValue())).forEach(rating -> {
                numberOfRatings.getAndIncrement();
                sum.addAndGet(rating.getScore().getValue());
            });
            bookWithRatingList.add(new BookWithRating(book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    getAvgScore(sum.get(), numberOfRatings.get()))
            );
        }
        return bookWithRatingList.stream().sorted(Comparator.comparingDouble(BookWithRating::getAvgScore).reversed()).limit(limit).toList();
    }

    private Double getAvgScore(int sum, int numberOfRating) {
        if (numberOfRating == 0) return 0.0;
        return (double) sum / numberOfRating;
    }
}
