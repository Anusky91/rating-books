package es.anusky.rating_books.books.infrastructure.controller;

import es.anusky.rating_books.books.application.bookdetails.GetBookDetailsService;
import es.anusky.rating_books.books.infrastructure.controller.responses.BookDetailsResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books")
@PreAuthorize("hasRole('USER')")
@RestController
@RequiredArgsConstructor
public class BookDetailsController {

    private final GetBookDetailsService service;

    @GetMapping("/books/{id}/details")
    public BookDetailsResponse getDetail(@PathVariable Long id) {
        return BookDetailsResponse.from(service.getDetails(id));
    }
}
