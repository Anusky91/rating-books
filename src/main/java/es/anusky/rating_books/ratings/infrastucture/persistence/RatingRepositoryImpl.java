package es.anusky.rating_books.ratings.infrastucture.persistence;

import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import es.anusky.rating_books.ratings.infrastucture.mapper.RatingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RatingRepositoryImpl implements RatingRepository {
    private final SpringDataRatingRepository springDataRatingRepository;

    @Override
    public Optional<Rating> findById(Long id) {
        return springDataRatingRepository.findById(id)
                .map(RatingMapper::toDomain);
    }

    @Override
    public Rating save(Rating rating) {
        var entity = RatingMapper.toEntity(rating);
        var saved = springDataRatingRepository.save(entity);
        return RatingMapper.toDomain(saved);
    }

    @Override
    public List<Rating> findAll() {
        return springDataRatingRepository.findAll()
                .stream()
                .map(RatingMapper::toDomain)
                .toList();
    }
}
