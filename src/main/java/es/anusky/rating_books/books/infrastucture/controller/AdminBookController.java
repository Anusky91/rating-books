package es.anusky.rating_books.books.infrastucture.controller;

import es.anusky.rating_books.books.application.BookService;
import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.shared.infrastructure.responses.BookResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;

    @PostMapping
    public BookResponse create(@RequestBody @Valid CreateBookRequest request) {
        Book created = bookService.createBook(
                request.title(),
                request.author(),
                request.editorial(),
                request.isbn(),
                request.publicationDate()
        );
        return BookResponse.from(created);
    }

    @PutMapping("/update")
    public BookResponse update(@RequestBody @Valid UpdateBookRequest request) {
        Book updated = bookService.update(request.id(), request.editorial(), request.publicationDate());
        return BookResponse.from(updated);
    }

    public record CreateBookRequest(
            @NotBlank(message = "El título es obligatorio")
            @Size(max = 200, message = "El título no puede tener más de 200 caracteres")
            String title,
            @NotBlank(message = "El autor es obligatorio")
            @Size(max = 100, message = "El autor no puede tener más de 100 caracteres")
            String author,
            @NotBlank(message = "La editorial es obligatoria")
            @Size(max = 100, message = "La editorial no puede tener más de 100 caracteres")
            String editorial,
            @NotBlank(message = "El ISBN es obligatorio")
            @Pattern(
                    regexp = "^97[89][- ]?\\d{1,5}[- ]?\\d{1,7}[- ]?\\d{1,7}[- ]?\\d$",
                    message = "Formato de ISBN inválido"
            )
            String isbn,
            @NotBlank(message = "La fecha de publicación es obligatoria")
            @Pattern(
                    regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
                    message = "Formato de fecha inválido"
            )
            String publicationDate
    ) {}

    public record UpdateBookRequest(
            @NotNull
            Long id,
            @Size(max = 100, message = "La editorial no puede tener más de 100 caracteres")
            String editorial,
            @Pattern(
                    regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
                    message = "Formato de fecha inválido"
            )
            String publicationDate
    ) {}
}
