package es.anusky.rating_books.favorite.infrastructure.controller;

import es.anusky.rating_books.favorite.application.FavoriteService;
import es.anusky.rating_books.shared.infrastructure.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/me/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService service;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse create(@RequestBody FavoriteCreateRequest request) {
        service.addFavorite(request.bookId());
        return new ApiResponse("Libro añadido a favoritos correctamente");
    }

    public record FavoriteCreateRequest(Long bookId){}
}
