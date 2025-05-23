package es.anusky.rating_books.users.application.publicprofile;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.favorite.domain.FavoriteRepository;
import es.anusky.rating_books.favorite.domain.model.Favorite;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import es.anusky.rating_books.shared.infrastructure.responses.FavoriteResponse;
import es.anusky.rating_books.shared.infrastructure.responses.UserPublicProfileResponse;
import es.anusky.rating_books.shared.infrastructure.security.AuthenticatedUserProvider;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final RatingRepository ratingRepository;
    private final AuthenticatedUserProvider userProvider;
    private final BookRepository bookRepository;

    public UserPublicProfileResponse getPublicProfile() {
        User user = extractUser();

        return UserPublicProfileResponse.builder()
                .userId(user.getUserId().getValue())
                .alias(user.getAlias().getValue())
                .age(calculateAge(user))
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .averageScore(getAverageScore(user))
                .favorites(getFavorites(user))
                .reviewsCount(ratingRepository.countByUserId(user.getUserId().getValue()))
                .build();
    }

    private List<FavoriteResponse> getFavorites(User user) {
        return favoriteRepository.findByUserId(user.getUserId().getValue())
                .stream()
                .sorted(Comparator.comparing(Favorite::getAddedAt).reversed())
                .map( fav -> {
                    Book book = bookRepository.findById(fav.getBookId().getValue())
                            .orElseThrow(() -> new BookNotFoundException("Book not found while parsing UserProfile response"));
                    return FavoriteResponse.toResponse(fav, book);
                }).toList();
    }

    private Double getAverageScore(User user) {
        List<Rating> ratings = ratingRepository.findByUserId(user.getUserId().getValue());
        if (ratings.isEmpty()) return 0.0;
        int sum = ratings.stream().mapToInt(r -> r.getScore().getValue()).sum();
        return BigDecimal.valueOf((double) sum / ratings.size())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private Integer calculateAge(User user) {
        LocalDate today = LocalDate.now();
        Integer years = today.getYear() - user.getBirthDate().getYear();
        if (today.getDayOfYear() < user.getBirthDate().getDayOfYear()) {
            return -- years;
        }
        return years;
    }

    private User extractUser() {
        String alias = userProvider.getCurrentAlias();
        return userRepository.findByAlias(alias).orElseThrow(
                () -> new UserNotFoundException("User with alias " + alias + " not found")
        );
    }
}
