package es.anusky.rating_books.users.domain.repository;

import es.anusky.rating_books.users.domain.model.User;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    List<User> findAll();
    Pair<User, String> create(User user);
    User update(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByAlias(String alias);

}
