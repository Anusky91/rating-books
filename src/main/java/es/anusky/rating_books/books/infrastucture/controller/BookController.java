package es.anusky.rating_books.books.infrastucture.controller;

import es.anusky.rating_books.books.application.BookService;
import es.anusky.rating_books.books.domain.model.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody @Valid CreateBookRequest request) {
        Book created = bookService.createBook(
                request.title(),
                request.author(),
                request.editorial(),
                request.isbn()
        );
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
            String isbn
    ) {}
}
