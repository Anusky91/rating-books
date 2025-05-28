package es.anusky.rating_books.books.infrastructure.controller;

import es.anusky.rating_books.books.application.BookService;
import es.anusky.rating_books.books.application.search.SearchBookService;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
import es.anusky.rating_books.shared.infrastructure.responses.BookResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Books")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final SearchBookService searchBookService;

    @GetMapping
    public List<BookResponse> findAll() {
        return bookService.findAll().stream().map(BookResponse::from).toList();
    }

    @GetMapping("/{id}")
    public BookResponse findById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(BookResponse::from)
                .orElseThrow(
                        () -> new BookNotFoundException("Book with ID " + id + " not found")
                );
    }

    @GetMapping("/search")
    public List<BookResponse> search(@RequestParam String query) {
        return searchBookService.search(query)
                .stream()
                .map(BookResponse::from)
                .toList();
    }
}
