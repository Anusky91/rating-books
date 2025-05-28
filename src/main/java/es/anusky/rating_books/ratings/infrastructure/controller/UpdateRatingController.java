package es.anusky.rating_books.ratings.infrastructure.controller;

import es.anusky.rating_books.cqrs.application.command.CommandBus;
import es.anusky.rating_books.cqrs.application.query.QueryBus;
import es.anusky.rating_books.cqrs.infrastructure.ApiController;
import es.anusky.rating_books.ratings.application.getrating.GetRatingByIdQuery;
import es.anusky.rating_books.ratings.application.updaterating.UpdateRatingCommand;
import es.anusky.rating_books.shared.infrastructure.responses.RatingResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rating")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/ratings/update")
public class UpdateRatingController extends ApiController {

    public UpdateRatingController(CommandBus commandBus, QueryBus queryBus) {
        super(commandBus, queryBus);
    }

    @PutMapping("/{id}")
    public RatingResponse update(@PathVariable Long id, @RequestBody UpdateRatingRequest request) {
        dispatch(new UpdateRatingCommand(id, request.score(), request.comment()));
        return ask(new GetRatingByIdQuery(id));
    }

    public record UpdateRatingRequest(
            @Min(1) @Max(5) int score,
            @NotBlank String comment) {
    }

}
