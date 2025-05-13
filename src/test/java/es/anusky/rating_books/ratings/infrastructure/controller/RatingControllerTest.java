package es.anusky.rating_books.ratings.infrastructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.ratings.domain.model.Rating;
import es.anusky.rating_books.ratings.domain.model.RatingMother;
import es.anusky.rating_books.shared.infrastructure.responses.RatingResponse;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RatingControllerTest extends IntegrationTestCase {

    @Test
    void test_post_controller() throws Exception {
        MvcResult result = mockMvc.perform(post("/ratings")
                        .header("Authorization", basicAuth())
                        .content(postBody())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    void test_findAll() throws Exception {
        Book book = bookRepository.save(BookMother.random());
        User user = userRepository.save(UserMother.random());
        for (int i = 0; i < 10; i++) {
            ratingRepository.save(RatingMother.with(book, user));
        }
        MvcResult result = mockMvc.perform(get("/ratings").header("Authorization", basicAuth()))
                .andExpect(status().isOk())
                .andReturn();
        RatingResponse[] responses = objectMapper.readValue(result.getResponse().getContentAsByteArray(), RatingResponse[].class);

        assertThat(responses.length).isGreaterThanOrEqualTo(10);
    }

    @Test
    void test_findById() throws Exception {
        Book book = bookRepository.save(BookMother.random());
        User user = userRepository.save(UserMother.random());
        for (int i = 0; i < 10; i++) {
            ratingRepository.save(RatingMother.with(book, user));
        }
        Rating saved = ratingRepository.save(RatingMother.with(book, user));

        MvcResult result = mockMvc.perform(get("/ratings/" + saved.getId().getValue()).header("Authorization", basicAuth()))
                .andExpect(status().isOk()).andReturn();

        RatingResponse response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), RatingResponse.class);

        assertNotNull(response);
        assertThat(response.date()).isAfterOrEqualTo(LocalDate.now());
    }

    @Test
    void test_rating_NotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/ratings/999").header("Authorization", basicAuth()))
                .andExpect(status().isNotFound())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Rating with ID 999 not found"));
    }

    @Test
    void test_rating_byBook_NotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/ratings/book/999").header("Authorization", basicAuth()))
                .andExpect(status().isNotFound())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Book with ID 999 doesn't exist"));
    }

    @Test
    void test_rating_already_exists() throws Exception {
        User user = userRepository.save(UserMother.random());
        Book book = bookRepository.save(BookMother.random());
        Rating rating = ratingRepository.save(RatingMother.with(book, user));

        RatingController.CreateRatingRequest request = new RatingController.CreateRatingRequest(rating.getBookId(),
                rating.getUserId().getValue(),
                3,
                "Cualquie cosa vale.");

        MvcResult result = mockMvc.perform(post("/ratings")
                .header("Authorization", basicAuth())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isConflict()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Already exists a rating for the this book"));
    }

    private String postBody() throws JsonProcessingException {
        Book book = bookRepository.save(BookMother.random());
        User user = userRepository.save(UserMother.random());
        RatingController.CreateRatingRequest request = new RatingController.CreateRatingRequest(book.getId().getValue(),
                user.getUserId().getValue(),
                3,
                "El mejor libro de autoayuda que he leido.");
        return objectMapper.writeValueAsString(request);
    }

}