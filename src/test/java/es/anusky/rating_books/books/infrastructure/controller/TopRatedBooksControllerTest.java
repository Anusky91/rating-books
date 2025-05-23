package es.anusky.rating_books.books.infrastructure.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.books.infrastructure.controller.responses.BookWithRatingResponse;
import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.ratings.domain.model.RatingMother;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TopRatedBooksControllerTest extends IntegrationTestCase {

    @Test
    void test_controller_top() throws Exception {
        createResources();
        MvcResult result = mockMvc.perform(get("/books/top")
                        .header("Authorization", basicAuthAdmin()))
                .andExpect(status().isOk()).andReturn();
        BookWithRatingResponse[] response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookWithRatingResponse[].class);

        assertThat(response.length).isEqualTo(10);
        for (int i = 1; i < response.length; i++) {
            assertThat(response[i - 1].getAverageRating())
                    .isGreaterThanOrEqualTo(response[i].getAverageRating());
        }

    }

    @Test
    void test_controller_top_5() throws Exception {
        createResources();
        MvcResult result = mockMvc.perform(get("/books/top?limit=5")
                        .header("Authorization", basicAuthAdmin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andReturn();
        BookWithRatingResponse[] response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookWithRatingResponse[].class);

        assertThat(response.length).isEqualTo(5);
        for (int i = 1; i < response.length; i++) {
            assertThat(response[i - 1].getAverageRating())
                    .isGreaterThanOrEqualTo(response[i].getAverageRating());
        }

    }

    private void createResources() {
        List<Book> books = new ArrayList<>();
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            books.add(bookRepository.save(BookMother.random()));
        }
        for (int i = 0; i < 12; i++) {
            users.add(userRepository.create(UserMother.random()).getFirst());
        }
        Collections.shuffle(books);
        Collections.shuffle(users);
        for (int i = 0; i < 150; i++) {
            int u = (int) (Math.random() * 10);
            int b = (int) (Math.random() * 10);
            ratingRepository.save(RatingMother.with(books.get(b), users.get(u)));
        }
    }

}