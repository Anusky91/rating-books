package es.anusky.rating_books.users.infrastructure.persistence;

import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import es.anusky.rating_books.users.infrastructure.mapper.UserMapper;
import es.anusky.rating_books.users.infrastructure.token.ActivationTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;
    private final UserMapper mapper;
    private final ActivationTokenGenerator generator;
    @Override
    public Optional<User> findById(Long id) {
        return springDataUserRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Pair<User, String> create(User user) {
        var entity = mapper.toEntity(user);
        var saved = springDataUserRepository.save(entity);
        String token = generator.generate(saved);
        return Pair.of(mapper.toDomain(saved), token);
    }

    @Override
    public User update(User user) {
        var entity = mapper.toEntity(user);
        var saved = springDataUserRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByAlias(String alias) {
        return springDataUserRepository.findByAlias(alias).map(mapper::toDomain);
    }
}
