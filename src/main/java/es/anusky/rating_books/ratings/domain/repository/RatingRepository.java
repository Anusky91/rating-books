package es.anusky.rating_books.ratings.domain.repository;

import es.anusky.rating_books.ratings.domain.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingRepository {
    Optional<Rating> findById(Long id);

    Rating save(Rating rating);

    List<Rating> findAll();

    List<Rating> findByBookId(Long bookId);
    List<Rating> findByUserId(Long userId);
    int countByUserId(Long userId);
}