package es.anusky.rating_books.books.application;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.books.domain.valueobjects.Author;
import es.anusky.rating_books.books.domain.valueobjects.Editorial;
import es.anusky.rating_books.books.domain.valueobjects.Isbn;
import es.anusky.rating_books.books.domain.valueobjects.Title;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.users.infrastructure.security.BookStarUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Book createBook(String title, String author, String editorial, String isbn, String publicationDate) {
        Book book = Book.create(
                new Title(title),
                new Author(author),
                new Editorial(editorial),
                new Isbn(isbn),
                LocalDate.parse(publicationDate)
        );
        var saved = bookRepository.save(book);
        publishEvent(saved, Actions.CREATE);
        return saved;
    }

    public Book update(Long bookId, String editorial, String publicationDate) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new BookNotFoundException("Book with ID " + bookId + " not found")
        );

        var saved = bookRepository.save(book.update(bookId,
                editorial,
                publicationDate));

        publishEvent(saved, Actions.UPDATE);
        return saved;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    private void publishEvent(Book saved, Actions actions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BookStarUserDetails user = (BookStarUserDetails) authentication.getPrincipal();
        eventPublisher.publishEvent(buidlAuditEvent(saved, actions, user));
    }

    private AuditEvent buidlAuditEvent(Book saved, Actions actions, BookStarUserDetails user) {
        return new AuditEvent(LocalDateTime.now(), Entities.BOOK.name(), saved.getId().getValue(), actions.name(), user.getUsername(), "ISBN: ".concat(saved.getIsbn().getValue()));
    }
}
