package es.anusky.rating_books.books.application.search;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.infrastructure.exception.IllegalQueryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchBookService {

    private final BookRepository bookRepository;

    public List<Book> search(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalQueryException("Query can`t be empty");
        }
        if (query.length() < 3) {
            throw new IllegalQueryException("Query must have at least 3 characters");
        }
        return bookRepository.findByTitleOrAuthor(query);
    }
}
