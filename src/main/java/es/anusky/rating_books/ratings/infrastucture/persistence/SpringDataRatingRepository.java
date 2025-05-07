package es.anusky.rating_books.ratings.infrastucture.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRatingRepository extends JpaRepository<RatingEntity, Long> {
}
