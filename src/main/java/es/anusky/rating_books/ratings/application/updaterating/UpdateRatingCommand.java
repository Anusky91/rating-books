package es.anusky.rating_books.ratings.application.updaterating;

import es.anusky.rating_books.cqrs.application.command.Command;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class UpdateRatingCommand implements Command {
    Long id;
    int score;
    String comment;
}
