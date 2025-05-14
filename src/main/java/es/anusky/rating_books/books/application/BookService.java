package es.anusky.rating_books.books.application;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.books.domain.valueobjects.*;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
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

    public Book update(Long bookId, String editorial, String publicationDate) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new BookNotFoundException("Book with ID " + bookId + " not found")
        );
        return bookRepository.save(book.update(bookId,
                editorial,
                publicationDate));
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }
}
