package es.anusky.rating_books.infrastructure;

import es.anusky.rating_books.cqrs.application.command.CommandBus;
import es.anusky.rating_books.cqrs.application.query.QueryBus;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class UnitTestCase {

    protected QueryBus queryBus;
    protected CommandBus commandBus;

    @BeforeEach
    protected void setUp() {
        queryBus = mock(QueryBus.class);
        commandBus = mock(CommandBus.class);
    }
}
