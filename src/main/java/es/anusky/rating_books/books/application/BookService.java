package es.anusky.rating_books.books.application;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.books.domain.valueobjects.Author;
import es.anusky.rating_books.books.domain.valueobjects.Editorial;
import es.anusky.rating_books.books.domain.valueobjects.Isbn;
import es.anusky.rating_books.books.domain.valueobjects.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Book createBook(String title, String author, String editorial, String isbn, String publicationDate) {
        Book book = Book.create(
                new Title(title),
                new Author(author),
                new Editorial(editorial),
                new Isbn(isbn),
                LocalDate.parse(publicationDate)
        );
        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }
}
