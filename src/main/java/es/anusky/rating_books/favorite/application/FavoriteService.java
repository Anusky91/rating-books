package es.anusky.rating_books.favorite.application;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.books.domain.valueobjects.BookId;
import es.anusky.rating_books.favorite.domain.FavoriteRepository;
import es.anusky.rating_books.favorite.domain.model.Favorite;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
import es.anusky.rating_books.infrastructure.exception.FavoriteAlreadyExistsException;
import es.anusky.rating_books.infrastructure.exception.NotFoundException;
import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.shared.infrastructure.responses.FavoriteResponse;
import es.anusky.rating_books.shared.infrastructure.security.AuthenticatedUserProvider;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BookRepository bookRepository;
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

    public List<FavoriteResponse> getFavoritesByUser() {
        User user = extractUser();
        return favoriteRepository.findByUserId(user.getUserId().getValue())
                .stream()
                .sorted(Comparator.comparing(Favorite::getAddedAt).reversed())
                .map( fav -> {
                    Book book = bookRepository.findById(fav.getBookId().getValue())
                            .orElseThrow(() -> new BookNotFoundException("Book not found while parsing Favorites"));
                    return FavoriteResponse.toResponse(fav, book);
                }).toList();
    }

    public void deleteFavorite(Long id) {
        User user = extractUser();
        Favorite favorites = favoriteRepository.findByIdAndUserId(id, user.getUserId().getValue()).orElseThrow(
                () -> new NotFoundException("Favorite not found for current user")
        );
        favoriteRepository.deleteById(id);
        eventPublisher.publishEvent(new AuditEvent(LocalDateTime.now(),
                Entities.FAVORITE.name(),
                id,
                Actions.DELETE.name(),
                user.getAlias().getValue(),
                "Favorite with ID " + id + "deleted correctly"));
    }

    private User extractUser() {
        String alias = userProvider.getCurrentAlias();
        return userRepository.findByAlias(alias).orElseThrow(
                () -> new UserNotFoundException("User with alias " + alias + " not found")
        );
    }
}
