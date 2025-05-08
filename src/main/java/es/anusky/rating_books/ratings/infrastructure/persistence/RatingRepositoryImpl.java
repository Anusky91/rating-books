package es.anusky.rating_books.ratings.infrastructure.persistence;

import es.anusky.rating_books.books.infrastucture.persistence.SpringDataBookRepository;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import es.anusky.rating_books.ratings.infrastructure.mapper.RatingMapper;
import es.anusky.rating_books.users.infrastructure.persistence.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RatingRepositoryImpl implements RatingRepository {
    private final SpringDataRatingRepository springDataRatingRepository;
    private final RatingMapper mapper;
    private final SpringDataBookRepository bookRepository;
    private final SpringDataUserRepository userRepository;

    @Override
    public Optional<Rating> findById(Long id) {
        return springDataRatingRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Rating save(Rating rating) {
        var bookEntity = bookRepository.findById(rating.getBookId()).orElseThrow(
                () -> new NoSuchElementException("Book not found with ID: " + rating.getBookId())
        );
        var userEntity = userRepository.findById(rating.getUserId().getValue()).orElseThrow(
                () -> new NoSuchElementException("User not found with ID: " + rating.getUserId().getValue())
        );
        var entity = mapper.toEntity(rating, bookEntity, userEntity);
        var saved = springDataRatingRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Rating> findAll() {
        return springDataRatingRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
