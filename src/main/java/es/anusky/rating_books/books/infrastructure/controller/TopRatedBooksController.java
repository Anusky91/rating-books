package es.anusky.rating_books.books.infrastructure.controller;

import es.anusky.rating_books.books.application.topratedbooks.GetTopRatedBooksService;
import es.anusky.rating_books.books.infrastructure.controller.responses.BookWithRatingResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Books")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/books/top")
@RequiredArgsConstructor
public class TopRatedBooksController {

    private final GetTopRatedBooksService topRatedBooksService;

    @GetMapping
    public List<BookWithRatingResponse> getTopRatedBooks(@RequestParam(defaultValue = "10") int limit) {
        return topRatedBooksService.execute(limit).stream()
                .map(BookWithRatingResponse::from)
                .toList();
    }
}
