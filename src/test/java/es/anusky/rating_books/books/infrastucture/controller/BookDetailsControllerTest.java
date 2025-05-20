package es.anusky.rating_books.books.infrastucture.controller;

import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.books.infrastucture.controller.responses.BookDetailsResponse;
import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.ratings.domain.model.RatingMother;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookDetailsControllerTest extends IntegrationTestCase {

    @Test
    void test_book_detail_controller() throws Exception {
        Book book = bookRepository.save(BookMother.random());
        for (int i = 0 ; i < 20 ; i++) {
            User user = userRepository.create(UserMother.random()).getFirst();
            ratingRepository.save(RatingMother.with(book, user));
        }

        MvcResult result = mockMvc.perform(get("/books/" + book.getId().getValue() + "/details")
                        .header("Authorization", basicAuth()))
                .andExpect(status().isOk())
                .andReturn();

        BookDetailsResponse response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookDetailsResponse.class);

        assertThat(response.getLastComments().size()).isLessThanOrEqualTo(10);
        assertThat(response.getTitle()).isEqualTo(book.getTitle().getValue());

    }

    @Test
    void test_book_detail_without_rating() throws Exception {
        Book book = bookRepository.save(BookMother.random());

        MvcResult result = mockMvc.perform(get("/books/" + book.getId().getValue() + "/details")
                        .header("Authorization", basicAuth()))
                .andExpect(status().isOk())
                .andReturn();

        BookDetailsResponse response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookDetailsResponse.class);

        assertThat(response.getLastComments().size()).isLessThanOrEqualTo(0);
        assertThat(response.getTitle()).isEqualTo(book.getTitle().getValue());

    }

    @Test
    void test_book_detail_exception() throws Exception {
        Book book = bookRepository.save(BookMother.random());

        MvcResult result = mockMvc.perform(get("/books/999/details")
                        .header("Authorization", basicAuth()))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).contains("Book with ID 999 not found");

    }

}