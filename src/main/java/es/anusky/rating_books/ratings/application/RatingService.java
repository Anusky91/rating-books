package es.anusky.rating_books.ratings.application;

import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.infrastructure.exception.BookAlreadyRatedByUserException;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
import es.anusky.rating_books.infrastructure.exception.RatingNotFoundException;
import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingComment;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingScore;
import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import es.anusky.rating_books.shared.infrastructure.security.AuthenticatedUserProvider;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticatedUserProvider userProvider;
    private final UserRepository userRepository;
    private final MeterRegistry meterRegistry;

    @Timed(value = "bookstar.rating.add.time", description = "Time taken to add a rating")
    public Rating create(Long bookId, int score, String comment) {
        meterRegistry.counter("bookstar.rating.add").increment();
        String alias = userProvider.getCurrentAlias();
        User user = userRepository.findByAlias(alias).orElseThrow(
                () -> new UserNotFoundException("User with alias " + alias + " not found")
        );
        if (checkUserHasAlreadyRatedABook(bookId, user.getUserId())) {
            throw new BookAlreadyRatedByUserException("Already exists a rating for this book");
        }
        Rating rating = Rating.create(bookId,
                new UserId(user.getUserId().getValue()),
                new RatingScore(score),
                new RatingComment(comment),
                LocalDate.now());

        var saved = ratingRepository.save(rating);
        publishEvent(saved, Actions.CREATE, alias);
        return saved;
    }

    public Optional<Rating> findById(Long id) {
        return ratingRepository.findById(id);
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public List<Rating> findByBookId(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Book with ID " + bookId + " doesn't exist");
        }
        return ratingRepository.findByBookId(bookId);
    }

    public Rating update(Long id, int score, String comment) {
        String alias = userProvider.getCurrentAlias();
        Optional<Rating> rating = ratingRepository.findById(id);
        if (rating.isEmpty()){
            throw new RatingNotFoundException("Rating with ID " + id + " not found");
        }
        var saved = ratingRepository.save(rating.get().update(score, comment));
        publishEvent(saved, Actions.UPDATE, alias);
        return saved;
    }

    private boolean checkUserHasAlreadyRatedABook(Long bookId, UserId userId) {
        List<Rating> ratings = findByBookId(bookId).stream()
                .filter(rating -> rating.getUserId().equals(userId))
                .toList();
        return !ratings.isEmpty();
    }

    private void publishEvent(Rating saved, Actions actions, String alias) {
        eventPublisher.publishEvent(buidlAuditEvent(saved, actions, alias));
    }

    private AuditEvent buidlAuditEvent(Rating saved, Actions actions, String alias) {
        return new AuditEvent(LocalDateTime.now(), Entities.RATING.name(), saved.getId().getValue(), actions.name(), alias, "Score: " + saved.getScore().getValue());
    }
}
