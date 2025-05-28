package es.anusky.rating_books.cqrs.application.query;

public interface QueryBus {
    <R, Q extends Query<R>> R ask(Q query);
}
