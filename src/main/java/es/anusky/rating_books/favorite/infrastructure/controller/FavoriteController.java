package es.anusky.rating_books.favorite.infrastructure.controller;

import es.anusky.rating_books.cqrs.application.command.CommandBus;
import es.anusky.rating_books.cqrs.application.query.QueryBus;
import es.anusky.rating_books.cqrs.infrastructure.ApiController;
import es.anusky.rating_books.favorite.application.FavoriteService;
import es.anusky.rating_books.favorite.application.addfavorite.AddFavoriteCommand;
import es.anusky.rating_books.shared.infrastructure.responses.ApiResponse;
import es.anusky.rating_books.shared.infrastructure.responses.FavoriteResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favorites")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/me/favorites")
public class FavoriteController extends ApiController {

    private final FavoriteService service;

    protected FavoriteController(CommandBus commandBus, QueryBus queryBus, FavoriteService favoriteService) {
        super(commandBus, queryBus);
        this.service = favoriteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse create(@RequestBody FavoriteCreateRequest request) {
        dispatch(new AddFavoriteCommand(request.bookId()));
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
