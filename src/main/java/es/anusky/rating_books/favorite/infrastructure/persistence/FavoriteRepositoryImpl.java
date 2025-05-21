package es.anusky.rating_books.favorite.infrastructure.persistence;

import es.anusky.rating_books.books.infrastructure.persistence.SpringDataBookRepository;
import es.anusky.rating_books.favorite.domain.FavoriteRepository;
import es.anusky.rating_books.favorite.domain.model.Favorite;
import es.anusky.rating_books.favorite.infrastructure.mapper.FavoriteMapper;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.users.infrastructure.persistence.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepository {

    private final SpringDataFavoriteRepository favoriteRepository;
    private final SpringDataBookRepository bookRepository;
    private final SpringDataUserRepository userRepository;
    public final FavoriteMapper mapper;

    @Override
    public Favorite save(Favorite favorite) {
        var user = userRepository.findById(favorite.getUserId().getValue()).orElseThrow(
                () -> new UserNotFoundException("User not found with ID: " + favorite.getUserId().getValue())
        );
        var book = bookRepository.findById(favorite.getBookId().getValue()).orElseThrow(
                () -> new BookNotFoundException("Book not found with ID: " + favorite.getBookId().getValue())
        );
        var entity = mapper.toEntity(favorite, book, user);
        var saved = favoriteRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Favorite> findById(Long id) {
        return favoriteRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Favorite> findAll() {
        return favoriteRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUser_Id(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByBookIdAndUserId(Long bookId, Long userId) {
        return favoriteRepository.existsByBook_IdAndUser_Id(bookId, userId);
    }
}
