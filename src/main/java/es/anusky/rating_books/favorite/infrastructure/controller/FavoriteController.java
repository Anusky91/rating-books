package es.anusky.rating_books.favorite.infrastructure.controller;

import es.anusky.rating_books.favorite.application.FavoriteService;
import es.anusky.rating_books.shared.infrastructure.responses.ApiResponse;
import es.anusky.rating_books.shared.infrastructure.responses.FavoriteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<FavoriteResponse> getFavorites() {
        return service.getFavoritesByUser();
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable Long id) {
        service.deleteFavorite(id);
        return new ApiResponse("Favorito borrado correctamente");
    }

    public record FavoriteCreateRequest(Long bookId){}
}
