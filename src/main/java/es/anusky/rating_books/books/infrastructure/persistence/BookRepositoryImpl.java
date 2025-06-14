package es.anusky.rating_books.books.infrastructure.persistence;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.books.infrastructure.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final SpringDataBookRepository springDataBookRepository;
    @Override
    public Book save(Book book) {
        var entity = BookMapper.toEntity(book);
        var saved = springDataBookRepository.save(entity);
        return BookMapper.toDomain(saved);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return springDataBookRepository.findById(id)
                .map(BookMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return springDataBookRepository.findAll()
                .stream()
                .map(BookMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        springDataBookRepository.deleteAll();
    }

    @Override
    public boolean existsById(Long id) {
        return springDataBookRepository.existsById(id);
    }

    @Override
    public List<Book> findByTitleOrAuthor(String query) {
        return springDataBookRepository.findByTitleIgnoreCaseContainingOrAuthorIgnoreCaseContaining(query, query)
                .stream()
                .map(BookMapper::toDomain)
                .toList();
    }
}
