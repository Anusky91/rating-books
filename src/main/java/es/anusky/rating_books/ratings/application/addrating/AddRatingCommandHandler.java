package es.anusky.rating_books.ratings.application.addrating;

import es.anusky.rating_books.cqrs.application.command.CommandHandler;
import es.anusky.rating_books.ratings.application.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddRatingCommandHandler implements CommandHandler<AddRatingCommand> {

    private final RatingService service;

    @Override
    public void handle(AddRatingCommand command) {
        service.create(command.getBookId(), command.getScore(), command.getComment());
    }
}
