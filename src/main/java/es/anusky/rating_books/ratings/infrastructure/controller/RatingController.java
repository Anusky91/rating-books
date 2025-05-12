package es.anusky.rating_books.ratings.infrastructure.controller;

import es.anusky.rating_books.infrastructure.exception.RatingNotFoundException;
import es.anusky.rating_books.ratings.application.RatingService;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.shared.infrastructure.responses.RatingResponse;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public RatingResponse save(@RequestBody CreateRatingRequest request) {
        Rating created = ratingService.create(request.bookId(),
                request.userId(),
                request.score(),
                request.comment());
        return RatingResponse.from(created);
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
                                      @NotNull Long userId,
                                      @Min(1) @Max(5) int score,
                                      @NotBlank @Size(max = 1000)String comment){}
}
