package es.anusky.rating_books.ratings.infrastructure.controller;

import es.anusky.rating_books.cqrs.application.command.CommandBus;
import es.anusky.rating_books.cqrs.application.query.QueryBus;
import es.anusky.rating_books.cqrs.infrastructure.ApiController;
import es.anusky.rating_books.ratings.application.getrating.GetUserRatingForBookQuery;
import es.anusky.rating_books.infrastructure.exception.RatingNotFoundException;
import es.anusky.rating_books.ratings.application.RatingService;
import es.anusky.rating_books.ratings.application.addrating.AddRatingCommand;
import es.anusky.rating_books.shared.infrastructure.responses.RatingResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Rating")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/ratings")
public class RatingController extends ApiController {
    private final RatingService ratingService;

    public RatingController(CommandBus commandBus, QueryBus queryBus, RatingService ratingService) {
        super(commandBus, queryBus);
        this.ratingService = ratingService;
    }

    @PostMapping
    public RatingResponse save(@RequestBody CreateRatingRequest request) {
        dispatch(new AddRatingCommand(request.bookId(), request.score(), request.comment()));
        return ask(new GetUserRatingForBookQuery(request.bookId()));
    }

    @GetMapping
    public List<RatingResponse> findAll() {
        return ratingService.findAll().stream().map(RatingResponse::from).toList();
    }

    @GetMapping("/{id}")
    private RatingResponse findById(@PathVariable Long id) {
        return ratingService.findById(id)
                .map(RatingResponse::from)
                .orElseThrow(
                        () -> new RatingNotFoundException("Rating with ID " + id + " not found")
                );
    }

    @GetMapping("/book/{bookId}")
    public List<RatingResponse> findByBookId(@PathVariable Long bookId) {
        return ratingService.findByBookId(bookId)
                .stream()
                .map(RatingResponse::from)
                .toList();
    }

    public record CreateRatingRequest(@NotNull Long bookId,
                                      @Min(1) @Max(5) int score,
                                      @NotBlank @Size(max = 1000)String comment){}
}
