package es.anusky.rating_books.books.infrastucture.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBookRepository extends JpaRepository<BookEntity, Long> {
}
