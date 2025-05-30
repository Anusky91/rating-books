package es.anusky.rating_books.favorite.application;

import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.books.domain.valueobjects.BookId;
import es.anusky.rating_books.books.infrastructure.persistence.BookRepositoryImpl;
import es.anusky.rating_books.favorite.domain.FavoriteRepository;
import es.anusky.rating_books.favorite.domain.model.Favorite;
import es.anusky.rating_books.favorite.domain.valueobjects.FavoriteId;
import es.anusky.rating_books.favorite.infrastructure.persistence.FavoriteRepositoryImpl;
import es.anusky.rating_books.infrastructure.exception.FavoriteAlreadyExistsException;
import es.anusky.rating_books.infrastructure.exception.NotFoundException;
import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import es.anusky.rating_books.shared.infrastructure.security.AuthenticatedUserProvider;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import es.anusky.rating_books.users.infrastructure.persistence.UserRepositoryImpl;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    FavoriteService service;
    FavoriteRepository favoriteRepository;
    BookRepository bookRepository;
    AuthenticatedUserProvider userProvider;
    UserRepository userRepository;
    ApplicationEventPublisher eventPublisher;
    MeterRegistry meterRegistry;
    Counter mockCounter;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepositoryImpl.class);
        bookRepository = mock(BookRepositoryImpl.class);
        userProvider = mock(AuthenticatedUserProvider.class);
        userRepository = mock(UserRepositoryImpl.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        meterRegistry = mock(MeterRegistry.class);
        mockCounter = mock(Counter.class);
        service = new FavoriteService(favoriteRepository, bookRepository, userProvider, userRepository, eventPublisher, meterRegistry);
        when(meterRegistry.counter(anyString())).thenReturn(mockCounter);
    }

    @Test
    void test_add_favorite() {
        User user = UserMother.randomWithId(5L);
        Favorite fav = new Favorite(new FavoriteId(23L), new BookId(12L), new UserId(5L), LocalDateTime.now());

        when(userProvider.getCurrentAlias()).thenReturn(user.getAlias().getValue());
        when(userRepository.findByAlias(anyString())).thenReturn(Optional.of(user));
        when(favoriteRepository.existsByBookIdAndUserId(anyLong(), anyLong())).thenReturn(false);
        when(favoriteRepository.save(any())).thenReturn(fav);

        assertDoesNotThrow(() -> service.addFavorite(12L));
        ArgumentCaptor<AuditEvent> captor = ArgumentCaptor.forClass(AuditEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        AuditEvent capturedEvent = captor.getValue();

        assertEquals("Book ID 12 marked as favorite", capturedEvent.details());

    }

    @Test
    void test_add_favorite_throws_exception() {
        User user = UserMother.randomWithId(5L);

        when(userProvider.getCurrentAlias()).thenReturn(user.getAlias().getValue());
        when(userRepository.findByAlias(anyString())).thenReturn(Optional.of(user));
        when(favoriteRepository.existsByBookIdAndUserId(anyLong(), anyLong())).thenReturn(true);

        assertThrows(FavoriteAlreadyExistsException.class, () -> service.addFavorite(12L));
        verify(meterRegistry).counter("bookstar.favorite.add");
        verify(mockCounter).increment();
        verify(favoriteRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void test_delete_favorite() {
        User user = UserMother.randomWithId(5L);
        Favorite fav = new Favorite(new FavoriteId(23L), new BookId(12L), new UserId(5L), LocalDateTime.now());

        when(userProvider.getCurrentAlias()).thenReturn(user.getAlias().getValue());
        when(userRepository.findByAlias(anyString())).thenReturn(Optional.of(user));
        when(favoriteRepository.findByIdAndUserId(23L, 5L)).thenReturn(Optional.of(fav));

        assertDoesNotThrow(() -> service.deleteFavorite(23L));
        ArgumentCaptor<AuditEvent> captor = ArgumentCaptor.forClass(AuditEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        AuditEvent capturedEvent = captor.getValue();

        assertEquals("Favorite with ID 23 deleted correctly", capturedEvent.details());
        verify(meterRegistry).counter("bookstar.favorite.remove");
        verify(mockCounter).increment();
    }

    @Test
    void test_delete_favorite_throws_exception() {
        User user = UserMother.randomWithId(5L);

        when(userProvider.getCurrentAlias()).thenReturn(user.getAlias().getValue());
        when(userRepository.findByAlias(anyString())).thenReturn(Optional.of(user));
        when(favoriteRepository.findByIdAndUserId(23L, 5L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,() -> service.deleteFavorite(23L));
        verify(meterRegistry).counter("bookstar.favorite.remove");
        verify(mockCounter).increment();
        verify(favoriteRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

}