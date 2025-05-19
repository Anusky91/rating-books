package es.anusky.rating_books.ratings.application;

import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.infrastructure.exception.BookAlreadyRatedByUserException;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
import es.anusky.rating_books.infrastructure.exception.RatingNotFoundException;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingComment;
import es.anusky.rating_books.ratings.domain.valueobjects.RatingScore;
import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import es.anusky.rating_books.users.infrastructure.security.BookStarUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Rating create(Long bookId, Long userId, int score, String comment) {
        if (checkUserHasAlreadyRatedABook(bookId, userId)) {
            throw new BookAlreadyRatedByUserException("Already exists a rating for the this book");
        }
        Rating rating = Rating.create(bookId,
                new UserId(userId),
                new RatingScore(score),
                new RatingComment(comment),
                LocalDate.now());

        var saved = ratingRepository.save(rating);
        publishEvent(saved, Actions.CREATE);
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
        Optional<Rating> rating = ratingRepository.findById(id);
        if (rating.isEmpty()){
            throw new RatingNotFoundException("Rating with ID " + id + " not found");
        }
        var saved = ratingRepository.save(rating.get().update(score, comment));
        publishEvent(saved, Actions.UPDATE);
        return saved;
    }

    private boolean checkUserHasAlreadyRatedABook(Long bookId, Long userId) {
        List<Rating> ratings = findByBookId(bookId).stream().filter(
                rating -> Objects.equals(rating.getUserId().getValue(), userId)
        ).toList();
        return !ratings.isEmpty();
    }

    private void publishEvent(Rating saved, Actions actions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BookStarUserDetails user = (BookStarUserDetails) authentication.getPrincipal();
        eventPublisher.publishEvent(new AuditEvent(LocalDateTime.now(), Entities.RATING.name(), saved.getId().getValue(), actions.name(), user.getUsername(), "Score: " + saved.getScore().getValue()));
    }
}
