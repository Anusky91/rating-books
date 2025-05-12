package es.anusky.rating_books.ratings.infrastructure.controller;

import es.anusky.rating_books.ratings.application.RatingService;
import es.anusky.rating_books.shared.infrastructure.responses.RatingResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings/update")
@RequiredArgsConstructor
public class UpdateRatingController {

    private final RatingService ratingService;

    @PutMapping("/{id}")
    public RatingResponse update(@PathVariable Long id, @RequestBody UpdateRatingRequest request) {
        return RatingResponse.from(ratingService.update(id, request.score(), request.comment()));
    }

    public record UpdateRatingRequest(
            @Min(1) @Max(5) int score,
            @NotBlank String comment) {
    }

}
