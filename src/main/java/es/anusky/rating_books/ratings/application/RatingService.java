package es.anusky.rating_books.ratings.application;

import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.infrastructure.exception.BookAlreadyRatedByUserException;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
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
    public void create(Long bookId, int score, String comment) {
        meterRegistry.counter("bookstar.rating.add").increment();
        String alias = userProvider.getCurrentAlias();
        User user = getUser(alias);
        if (checkUserHasAlreadyRatedABook(bookId, user.getUserId())) {
            throw new BookAlreadyRatedByUserException("Already exists a rating for this book");
        }
        Rating rating = Rating.create(bookId,
                new UserId(user.getUserId().getValue()),
                new RatingScore(score),
                new RatingComment(comment),
                LocalDate.now());

        var saved = ratingRepository.save(rating);
        eventPublisher.publishEvent(buidlAuditEvent(saved, alias));
    }

    public Optional<Rating> findByUserIdAndBookId(Long bookId) {
        String alias = userProvider.getCurrentAlias();
        User user = getUser(alias);
        return ratingRepository.findByUserId(user.getUserId().getValue())
                .stream()
                .filter(r -> r.getBookId().equals(bookId))
                .findFirst();
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

    private User getUser(String alias) {
        return userRepository.findByAlias(alias).orElseThrow(
                () -> new UserNotFoundException("User with alias " + alias + " not found")
        );
    }

    private boolean checkUserHasAlreadyRatedABook(Long bookId, UserId userId) {
        List<Rating> ratings = findByBookId(bookId).stream()
                .filter(rating -> rating.getUserId().equals(userId))
                .toList();
        return !ratings.isEmpty();
    }

    private AuditEvent buidlAuditEvent(Rating saved, String alias) {
        return new AuditEvent(LocalDateTime.now(), Entities.RATING.name(), saved.getId().getValue(), Actions.CREATE.name(), alias, "Score: " + saved.getScore().getValue());
    }
}
