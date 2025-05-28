package es.anusky.rating_books.cqrs.infrastructure;

import es.anusky.rating_books.cqrs.application.command.Command;
import es.anusky.rating_books.cqrs.application.command.CommandBus;
import es.anusky.rating_books.cqrs.application.query.Query;
import es.anusky.rating_books.cqrs.application.query.QueryBus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiController {
    protected final CommandBus commandBus;
    protected final QueryBus queryBus;

    protected ApiController(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    protected <C extends Command> void dispatch(C command) {
        log.info("Dispatching command: {} → {}", command.getClass().getSimpleName(), command);
        commandBus.dispatch(command);
    }

    protected <R, Q extends Query<R>> R ask(Q query) {
        log.info("Asking query: {} → {}", query.getClass().getSimpleName(), query);
        return queryBus.ask(query);
    }
}
