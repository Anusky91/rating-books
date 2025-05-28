package es.anusky.rating_books.favorite.application.addfavorite;

import es.anusky.rating_books.cqrs.application.command.CommandHandler;
import es.anusky.rating_books.favorite.application.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddFavoriteCommandHandler implements CommandHandler<AddFavoriteCommand> {

    private final FavoriteService service;

    @Override
    public void handle(AddFavoriteCommand command) {
        service.addFavorite(command.getBookId());
    }
}
