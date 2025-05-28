package es.anusky.rating_books.ratings.application.updaterating;

import es.anusky.rating_books.cqrs.application.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateRatingCommandHandler implements CommandHandler<UpdateRatingCommand> {

    private final UpdateRatingService updateService;

    @Override
    public void handle(UpdateRatingCommand command) {
        updateService.update(command.getId(), command.getScore(), command.getComment());
    }
}
