package es.anusky.rating_books.cqrs.application.command;

public interface CommandBus {
    <C extends Command> void dispatch(C command);
}
