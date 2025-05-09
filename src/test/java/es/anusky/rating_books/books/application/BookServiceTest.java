package es.anusky.rating_books.books.application;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void should_insert_a_new_book() {
        Book book = BookMother.random();
        bookRepository.save(book);
        List<Book> books = bookService.findAll();
        assertEquals(1, books.size());
    }

    @Test
    void should_find_book_by_id() {
        Book book = BookMother.random();
        Book book2 = bookRepository.save(book);
        Optional<Book> bookOp = bookService.findById(book2.getId().getValue());
        assertThat(book).usingRecursiveComparison().ignoringFields("id").isEqualTo(book2);
        assertEquals(book.getTitle().getValue() ,bookOp.get().getTitle().getValue());
    }

    @Test
    void should_return_empty_when_book_not_found() {
        Optional<Book> book = bookService.findById(999L);
        assertThat(book).isEmpty();
    }

}