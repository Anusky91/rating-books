package es.anusky.rating_books.favorite.domain;

import es.anusky.rating_books.favorite.domain.model.Favorite;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository {
    Favorite save(Favorite favorite);
    Optional<Favorite> findById(Long id);
    List<Favorite> findAll();
    List<Favorite> findByUserId(Long userId);
    boolean existsByBookIdAndUserId(Long bookId, Long userId);
    void deleteById(Long id);
    Optional<Favorite> findByIdAndUserId(Long id, Long userId);
}
