package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.favorite.domain.model.Favorite;
import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.ratings.domain.model.RatingMother;
import es.anusky.rating_books.shared.infrastructure.responses.UserPublicProfileResponse;
import es.anusky.rating_books.users.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserProfileControllerTest extends IntegrationTestCase {

    @Test
    void test_user_profile() throws Exception {
        String alias = "userTest";
        createScenario(alias);

        MvcResult result = mockMvc.perform(get("/me/profile")
                        .header("Authorization", basicAuth(alias, PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();

        UserPublicProfileResponse response = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                UserPublicProfileResponse.class);

        assertThat(response.getFavorites().size()).isGreaterThanOrEqualTo(5);
        assertThat(response.getReviewsCount()).isGreaterThanOrEqualTo(15);
        assertThat(response.getAlias()).isEqualTo(alias);
        assertThat(response.getAverageScore()).isGreaterThanOrEqualTo(1.0);
    }

    private void createScenario(String alias) {
        User user = createUserTest(alias);

        for (int i = 0; i < 15; i++) {
            Book book = bookRepository.save(BookMother.random());
            ratingRepository.save(RatingMother.with(book, user));
            if (i % 3 == 0) {
                favoriteRepository.save(Favorite.create(book.getId(), user.getUserId()));
            }
        }
    }

}