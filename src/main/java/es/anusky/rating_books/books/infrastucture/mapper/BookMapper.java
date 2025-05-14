package es.anusky.rating_books.books.infrastucture.mapper;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.valueobjects.*;
import es.anusky.rating_books.books.infrastucture.persistence.BookEntity;

public class BookMapper {

    public static BookEntity toEntity(Book book) {
        return BookEntity.builder()
                .id(book.getId() != null ? book.getId().getValue() : null)
                .title(book.getTitle().getValue())
                .author(book.getAuthor().getValue())
                .editorial(book.getEditorial().getValue())
                .isbn(book.getIsbn().getValue())
                .publicationDate(book.getPublicationDate())
                .build();
    }

    public static Book toDomain(BookEntity entity) {
        return new Book(
                new BookId(entity.getId()),
                new Title(entity.getTitle()),
                new Author(entity.getAuthor()),
                new Editorial(entity.getEditorial()),
                new Isbn(entity.getIsbn()),
                entity.getPublicationDate()
        );
    }

}
