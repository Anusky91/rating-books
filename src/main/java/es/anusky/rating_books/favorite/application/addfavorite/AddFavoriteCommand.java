package es.anusky.rating_books.favorite.application.addfavorite;

import es.anusky.rating_books.cqrs.application.command.Command;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AddFavoriteCommand implements Command {
    Long bookId;

    public AddFavoriteCommand(Long bookId) {
        super();
        this.bookId = bookId;
    }
}
