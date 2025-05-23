package es.anusky.rating_books.favorite.application;

import es.anusky.rating_books.books.domain.valueobjects.BookId;
import es.anusky.rating_books.favorite.domain.FavoriteRepository;
import es.anusky.rating_books.favorite.domain.model.Favorite;
import es.anusky.rating_books.infrastructure.exception.FavoriteAlreadyExistsException;
import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.shared.infrastructure.security.AuthenticatedUserProvider;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final AuthenticatedUserProvider userProvider;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void addFavorite(Long bookId) {
        User user = extractUser();
        if (favoriteRepository.existsByBookIdAndUserId(bookId, user.getUserId().getValue())) {
            throw new FavoriteAlreadyExistsException(bookId, user.getUserId().getValue());
        }
        Favorite favorite = Favorite.create(new BookId(bookId), user.getUserId());
        var saved = favoriteRepository.save(favorite);
        eventPublisher.publishEvent(new AuditEvent(LocalDateTime.now(),
                Entities.FAVORITE.name(),
                saved.getFavoriteId().getValue(),
                Actions.CREATE.name(), user.getAlias().getValue(),
                "Book ID " + bookId + " marked as favorite"));
    }

    private User extractUser() {
        String alias = userProvider.getCurrentAlias();
        return userRepository.findByAlias(alias).orElseThrow(
                () -> new UserNotFoundException("User with alias " + alias + " not found")
        );
    }
}
