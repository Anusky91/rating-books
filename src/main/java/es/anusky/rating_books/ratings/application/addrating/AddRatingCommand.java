package es.anusky.rating_books.ratings.application.addrating;

import es.anusky.rating_books.cqrs.application.command.Command;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
@ToString
public class AddRatingCommand implements Command {
    Long bookId;
    int score;
    String comment;

    public AddRatingCommand(Long bookId, int score, String comment) {
        super();
        this.bookId = bookId;
        this.score = score;
        this.comment = comment;
    }
}
