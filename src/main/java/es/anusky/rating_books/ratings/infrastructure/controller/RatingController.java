package es.anusky.rating_books.ratings.infrastructure.controller;

import es.anusky.rating_books.ratings.application.RatingService;
import es.anusky.rating_books.ratings.domain.model.Rating;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Rating> save(@RequestBody CreateRatingRequest request) {
        Rating created = ratingService.create(request.bookId(),
                request.userId(),
                request.score(),
                request.comment());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public List<Rating> findAll() {
        return ratingService.findAll();
    }

    @GetMapping("/{id}")
    private ResponseEntity<Rating> findById(@PathVariable Long id) {
        return ratingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public record CreateRatingRequest(@NotNull Long bookId,
                                      @NotNull Long userId,
                                      @Min(1) @Max(5) int score,
                                      @NotBlank @Size(max = 1000)String comment){}
}
