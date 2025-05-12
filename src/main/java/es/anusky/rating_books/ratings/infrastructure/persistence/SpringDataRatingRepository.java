package es.anusky.rating_books.ratings.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataRatingRepository extends JpaRepository<RatingEntity, Long> {
    List<RatingEntity> findByBookId(Long id);
}
