package es.anusky.rating_books.users.application.publicprofile;

import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.books.domain.valueobjects.BookId;
import es.anusky.rating_books.books.infrastructure.persistence.BookRepositoryImpl;
import es.anusky.rating_books.favorite.domain.FavoriteRepository;
import es.anusky.rating_books.favorite.domain.model.Favorite;
import es.anusky.rating_books.favorite.domain.valueobjects.FavoriteId;
import es.anusky.rating_books.favorite.infrastructure.persistence.FavoriteRepositoryImpl;
import es.anusky.rating_books.infrastructure.exception.BookNotFoundException;
import es.anusky.rating_books.infrastructure.exception.UserNotFoundException;
import es.anusky.rating_books.ratings.domain.model.RatingMother;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import es.anusky.rating_books.ratings.infrastructure.persistence.RatingRepositoryImpl;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import es.anusky.rating_books.shared.infrastructure.responses.UserPublicProfileResponse;
import es.anusky.rating_books.shared.infrastructure.security.AuthenticatedUserProvider;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import es.anusky.rating_books.users.infrastructure.persistence.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserProfileServiceTest {

    UserRepository userRepository;
    FavoriteRepository favoriteRepository;
    RatingRepository ratingRepository;
    AuthenticatedUserProvider userProvider;
    BookRepository bookRepository;
    UserProfileService service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepositoryImpl.class);
        favoriteRepository = mock(FavoriteRepositoryImpl.class);
        ratingRepository = mock(RatingRepositoryImpl.class);
        userProvider = mock(AuthenticatedUserProvider.class);
        bookRepository = mock(BookRepositoryImpl.class);
        service = new UserProfileService(userRepository, favoriteRepository, ratingRepository, userProvider, bookRepository);
    }

    @Test
    void test_happy_path() {
        User user = UserMother.randomWithId(5L);

        when(userProvider.getCurrentAlias()).thenReturn(user.getAlias().getValue());
        when(userRepository.findByAlias(anyString())).thenReturn(Optional.of(user));
        when(ratingRepository.findByUserId(anyLong())).thenReturn(List.of(RatingMother.with(BookMother.randomWithId(4L), user)));
        when(favoriteRepository.findByUserId(anyLong())).thenReturn(List.of());
        when(ratingRepository.countByUserId(anyLong())).thenReturn(1);

        UserPublicProfileResponse response = service.getPublicProfile();

        assertThat(response.getAlias()).isEqualTo(user.getAlias().getValue());
        assertThat(response.getReviewsCount()).isEqualTo(1);
        assertThat(response.getAge()).isEqualTo(34);
    }

    @Test
    void test_throws_UserNotFoundException() {
        User user = UserMother.randomWithId(5L);

        when(userRepository.findByAlias(anyString())).thenReturn(Optional.of(user));
        when(ratingRepository.findByUserId(anyLong())).thenReturn(List.of(RatingMother.with(BookMother.randomWithId(4L), user)));
        when(favoriteRepository.findByUserId(anyLong())).thenReturn(List.of());
        when(ratingRepository.countByUserId(anyLong())).thenReturn(1);

        assertThrows(UserNotFoundException.class, () -> service.getPublicProfile());
    }

    @Test
    void test_throws_BookNotFoundException() {
        User user = UserMother.randomWithId(5L);

        when(userProvider.getCurrentAlias()).thenReturn(user.getAlias().getValue());
        when(userRepository.findByAlias(anyString())).thenReturn(Optional.of(user));
        when(ratingRepository.findByUserId(anyLong())).thenReturn(List.of(RatingMother.with(BookMother.randomWithId(4L), user)));
        when(favoriteRepository.findByUserId(anyLong())).thenReturn(List.of(new Favorite(new FavoriteId(1L),
                new BookId(8L),
                new UserId(user.getUserId().getValue()),
                LocalDateTime.now().minusHours(6))));
        when(ratingRepository.countByUserId(anyLong())).thenReturn(1);

        assertThrows(BookNotFoundException.class, () -> service.getPublicProfile());
    }

}