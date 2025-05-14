package es.anusky.rating_books.books.infrastucture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.users.domain.model.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminBookControllerTest extends IntegrationTestCase {

    @Test
    void test_post_controller() throws Exception {
        MvcResult result = mockMvc.perform(createRequestPost()).andExpect(status().isOk()).andReturn();
        assertNotNull(result.getResponse().getContentAsString());
    }

    private MockHttpServletRequestBuilder createRequestPost() throws JsonProcessingException {
        return post("/admin/books").header("Authorization", basicAuth())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createPostBodyRequest())
                .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    private String createPostBodyRequest() throws JsonProcessingException {
        Book example = BookMother.random();
        AdminBookController.CreateBookRequest request = new AdminBookController.CreateBookRequest(example.getTitle().getValue(),
                example.getAuthor().getValue(), example.getEditorial().getValue(), example.getIsbn().getValue());
        return objectMapper.writeValueAsString(request);
    }

}