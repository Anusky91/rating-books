package es.anusky.rating_books.books.domain.repository;

import es.anusky.rating_books.books.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(Long id);
    List<Book> findAll();
    void deleteAll();
    boolean existsById(Long id);
    List<Book> findByTitleOrAuthor(String query);
}
