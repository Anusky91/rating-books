package es.anusky.rating_books.cqrs.application.command;

public interface CommandHandler<C extends Command> {
    void handle(C command);
}
