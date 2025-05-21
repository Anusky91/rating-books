package es.anusky.rating_books.books.infrastructure.controller;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerTest extends IntegrationTestCase {

    @Test
    @Sql(scripts = "/sql-scripts/books-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void test_get_findAll() throws Exception {
        Set<String> expectedTitles = Set.of("El nombre del viento", "1984", "Fahrenheit 451", "La sombra del viento", "Cien años de soledad");
        MvcResult result = mockMvc.perform(createRequestGet()).andExpect(status().isOk()).andReturn();
        BookResponse[] books = objectMapper.readValue(result.getResponse().getContentAsString(), BookResponse[].class);
        List<String> actualTitles = new ArrayList<>();
        for (BookResponse book : books) {
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
        MvcResult result = mockMvc.perform(get("/books/1")
                        .header("Authorization", basicAuth())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        BookResponse bookResponse = objectMapper.readValue(result.getResponse().getContentAsString(), BookResponse.class);
        assertEquals(1, bookResponse.id());
    }

    @Test
    void test_get_search() throws Exception {
        Book book = BookMother.random();
        bookRepository.save(book);
        MvcResult result = mockMvc.perform(get("/books/search?query=" + book.getTitle().getValue())
                        .header("Authorization", basicAuth()))
                .andExpect(status().isOk())
                .andReturn();
        BookResponse[] responses = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookResponse[].class);
        assertThat(responses.length).isGreaterThanOrEqualTo(1);
    }

    @Test
    void test_get_search_exception() throws Exception {
        Book book = BookMother.random();
        bookRepository.save(book);
        MvcResult result = mockMvc.perform(get("/books/search?query=z")
                        .header("Authorization", basicAuth()))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Query must have at least 3 characters");
    }

    @Test
    void test_get_search_empty_list() throws Exception {
        Book book = BookMother.random();
        bookRepository.save(book);
        MvcResult result = mockMvc.perform(get("/books/search?query=zzzzzzz")
                        .header("Authorization", basicAuth()))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo("[]");
    }

    private MockHttpServletRequestBuilder createRequestGet() {
        return get("/books").header("Authorization", basicAuth()).contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private MockHttpServletRequestBuilder createRequestGetById() {
        return get("/books/999").header("Authorization", basicAuth()).contentType(MediaType.APPLICATION_JSON_VALUE);
    }

}