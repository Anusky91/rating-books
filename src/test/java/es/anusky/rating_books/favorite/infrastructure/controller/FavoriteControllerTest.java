package es.anusky.rating_books.favorite.infrastructure.controller;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.books.domain.valueobjects.BookId;
import es.anusky.rating_books.favorite.domain.model.Favorite;
import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.shared.domain.valueobjects.UserId;
import es.anusky.rating_books.shared.infrastructure.responses.FavoriteResponse;
import es.anusky.rating_books.users.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavoriteControllerTest extends IntegrationTestCase {

    @Test
    void test_create_new_favorite_200() throws Exception {
        String alias = "testFav";
        createUserTest(alias);
        Book book = bookRepository.save(BookMother.random());

        FavoriteController.FavoriteCreateRequest request = new FavoriteController.FavoriteCreateRequest(book.getId().getValue());

        MvcResult result = mockMvc.perform(post("/me/favorites")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", basicAuth(alias, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("{\"message\":\"Libro añadido a favoritos correctamente\"}");

    }

    @Test
    void test_get_favorites() throws Exception {
        String alias = "testFav";
        createScenario("testFav");

        MvcResult result = mockMvc.perform(get("/me/favorites")
                        .header("Authorization", basicAuth(alias, PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();

        FavoriteResponse[] responses = objectMapper.readValue(result.getResponse().getContentAsByteArray(), FavoriteResponse[].class);
        assertThat(responses.length).isGreaterThanOrEqualTo(10);
    }

    @Test
    void should_delete_favorite_by_id_when_authenticated() throws Exception {
        String alias = "testFav";
        List<Long> ids = createScenario(alias);

        MvcResult result = mockMvc.perform(delete("/me/favorites/" + ids.getFirst())
                        .header("Authorization", basicAuth(alias, PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("{\"message\":\"Favorito borrado correctamente\"}");

    }

    private List<Long> createScenario(String alias) {
        User user = createUserTest(alias);
        List<Long> favIds = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Book book = bookRepository.save(BookMother.random());
            Favorite fav = favoriteRepository.save(new Favorite(null,
                    new BookId(book.getId().getValue()),
                    new UserId(user.getUserId().getValue()),
                    LocalDateTime.of(2025, 4, 12, 12, i)));

            favIds.add(fav.getFavoriteId().getValue());
        }
        return favIds;
    }

}