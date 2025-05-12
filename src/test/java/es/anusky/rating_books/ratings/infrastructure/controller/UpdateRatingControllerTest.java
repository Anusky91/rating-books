package es.anusky.rating_books.ratings.infrastructure.controller;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateRatingControllerTest extends IntegrationTestCase {

    @Test
    void test_update_controller() throws Exception {
        User user = userRepository.save(UserMother.random());
        Book book = bookRepository.save(BookMother.random());
        Rating actualRating = ratingRepository.save(RatingMother.with(book, user));

        UpdateRatingController.UpdateRatingRequest request = getUpdateRatingRequest();

        MvcResult result = mockMvc.perform(put("/ratings/update/" + actualRating.getId().getValue())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        RatingResponse response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), RatingResponse.class);
        assertThat(response.comment()).isEqualTo(request.comment());
        assertThat(response.score()).isEqualTo(request.score());
    }

    private static UpdateRatingController.UpdateRatingRequest getUpdateRatingRequest() {
        UpdateRatingController.UpdateRatingRequest request = new UpdateRatingController.UpdateRatingRequest(5,
                "El mejor libro que le leido en mi vida, muy inspirador. 100% recomendado!");
        return request;
    }

    @Test
    void test_update_controller_exception() throws Exception {
        MvcResult result = mockMvc.perform(put("/ratings/update/" + 999)
                        .content(objectMapper.writeValueAsString(getUpdateRatingRequest()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).contains("Rating with ID 999 not found");
    }

}