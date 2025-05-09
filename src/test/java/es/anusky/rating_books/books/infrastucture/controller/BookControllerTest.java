package es.anusky.rating_books.books.infrastucture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.shared.infrastructure.responses.BookResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerTest extends IntegrationTestCase {

    @Test
    void test_post_controller() throws Exception {
        MvcResult result = mockMvc.perform(createRequestPost()).andExpect(status().isOk()).andReturn();
        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    @Sql(scripts = "/sql-scripts/books-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void test_get_findAll() throws Exception {
        Set<String> expectedTitles = Set.of("El nombre del viento", "1984", "Fahrenheit 451", "La sombra del viento", "Cien años de soledad");
        MvcResult result = mockMvc.perform(createRequestGet()).andExpect(status().isOk()).andReturn();
        BookResponse[] books = objectMapper.readValue(result.getResponse().getContentAsString(), BookResponse[].class);
        List<String> actualTitles = new ArrayList<>();
        for (BookResponse book: books) {
            actualTitles.add(book.title());
        }
        assertTrue(actualTitles.containsAll(expectedTitles));
    }

    @Test
    void test_get_findById_NotFound() throws Exception {
        Book book = BookMother.random();
        bookRepository.save(book);
        MvcResult result = mockMvc.perform(createRequestGetById()).andExpect(status().is4xxClientError()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Book with ID 999 not found"));
    }

    @Test
    void test_get_findById_shouldReturnBook() throws Exception {
        Book book = BookMother.random();
        bookRepository.save(book);
        MvcResult result = mockMvc.perform(get("/books/1").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        BookResponse bookResponse = objectMapper.readValue(result.getResponse().getContentAsString(), BookResponse.class);
        assertEquals(1, bookResponse.id());
    }

    private MockHttpServletRequestBuilder createRequestPost() throws JsonProcessingException {
        return post("/books").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createPostBodyRequest()).accept(MediaType.APPLICATION_JSON_VALUE);
    }

    private MockHttpServletRequestBuilder createRequestGet() {
        return get("/books").contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private MockHttpServletRequestBuilder createRequestGetById() {
        return get("/books/999").contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private String createPostBodyRequest() throws JsonProcessingException {
        Book example = BookMother.random();
        BookController.CreateBookRequest request = new BookController.CreateBookRequest(example.getTitle().getValue(),
                example.getAuthor().getValue(), example.getEditorial().getValue(), example.getIsbn().getValue());
        return objectMapper.writeValueAsString(request);
    }
}