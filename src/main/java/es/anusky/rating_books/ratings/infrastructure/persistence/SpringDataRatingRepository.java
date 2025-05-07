package es.anusky.rating_books.ratings.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRatingRepository extends JpaRepository<RatingEntity, Long> {
}
