package es.anusky.rating_books.favorite.infrastructure.controller;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavoriteControllerTest extends IntegrationTestCase {

    @Test
    void test_create_new_favorite_200() throws Exception {
        User user = userRepository.create(UserMother.withAliasAndPassword("testFav", "TestFav12!*")).getFirst();
        Book book = bookRepository.save(BookMother.random());

        FavoriteController.FavoriteCreateRequest request = new FavoriteController.FavoriteCreateRequest(book.getId().getValue());

        MvcResult result = mockMvc.perform(post("/me/favorites")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", basicAuth("testFav", "TestFav12!*"))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("{\"message\":\"Libro añadido a favoritos correctamente\"}");

    }

}