package es.anusky.rating_books.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.anusky.rating_books.RatingBooksApplication;
import es.anusky.rating_books.books.domain.repository.BookRepository;
import es.anusky.rating_books.favorite.domain.FavoriteRepository;
import es.anusky.rating_books.ratings.domain.repository.RatingRepository;
import es.anusky.rating_books.shared.infrastructure.audit.SpringDataAuditRepository;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import es.anusky.rating_books.users.domain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.Optional;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RatingBooksApplication.class)
public class IntegrationTestCase {

    public static final String PASSWORD = "TestFav12!*";

    protected static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired protected MockMvc mockMvc;
    @Autowired protected BookRepository bookRepository;
    @Autowired protected RatingRepository ratingRepository;
    @Autowired protected UserRepository userRepository;
    @Autowired protected FavoriteRepository favoriteRepository;
    @Autowired protected SpringDataAuditRepository auditRepository;

    @PostConstruct
    public void ensureAdminUserExists() {
        userRepository.findByAlias("adminTest").orElseGet(() -> userRepository.create(UserMother.withAdminRole()).getFirst());
    }


    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public String basicAuthAdmin() {
        String credentials = "adminTest:passwordTest8!";
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public String basicAuth(String alias, String password) {
        String credentials = alias + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public User createUserTest(String alias) {
        Optional<User> user = userRepository.findByAlias(alias);
        return user.orElseGet(() -> userRepository.create(UserMother.withAliasAndPassword(alias, PASSWORD)).getFirst());
    }

}
