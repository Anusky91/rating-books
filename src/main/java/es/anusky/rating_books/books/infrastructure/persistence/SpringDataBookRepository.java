package es.anusky.rating_books.books.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataBookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findByTitleIgnoreCaseContainingOrAuthorIgnoreCaseContaining(String title, String author);
}
