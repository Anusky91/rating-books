package es.anusky.rating_books.cqrs.application.query;

public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}
