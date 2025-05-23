package es.anusky.rating_books.favorite.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataFavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    List<FavoriteEntity> findByUser_Id(Long userId);
    //boolean existsByBookAndUser(BookEntity book, UserEntity user);
    boolean existsByBook_IdAndUser_Id(Long bookId, Long userId);
    Optional<FavoriteEntity> findByIdAndUser_Id(Long id, Long userId);

}
