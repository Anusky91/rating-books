package es.anusky.rating_books.favorite.application;

import es.anusky.rating_books.favorite.domain.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    public final FavoriteRepository favoriteRepository;
}
