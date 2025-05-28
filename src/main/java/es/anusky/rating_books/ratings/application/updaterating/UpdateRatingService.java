package es.anusky.rating_books.ratings.application.updaterating;

import es.anusky.rating_books.infrastructure.exception.RatingNotFoundException;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.shared.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateRatingService {

    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticatedUserProvider userProvider;
    private final RatingRepository ratingRepository;

    public void update(Long id, int score, String comment) {
        String alias = userProvider.getCurrentAlias();
        Rating rating = ratingRepository.findById(id).orElseThrow(
                () -> new RatingNotFoundException("Rating with ID " + id + " not found")
        );
        var saved = ratingRepository.save(rating.update(score, comment));
        eventPublisher.publishEvent(buidlAuditEvent(saved, alias));
    }

    private AuditEvent buidlAuditEvent(Rating saved, String alias) {
        return new AuditEvent(LocalDateTime.now(),
                Entities.RATING.name(),
                saved.getId().getValue(),
                Actions.UPDATE.name(),
                alias,
                "Score: " + saved.getScore().getValue());
    }
}
