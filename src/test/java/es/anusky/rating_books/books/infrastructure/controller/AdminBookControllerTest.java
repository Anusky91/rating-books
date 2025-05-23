package es.anusky.rating_books.books.infrastructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.anusky.rating_books.books.domain.model.Book;
import es.anusky.rating_books.books.domain.model.BookMother;
import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.shared.domain.enums.Entities;
import es.anusky.rating_books.shared.infrastructure.audit.AuditLogEntity;
import es.anusky.rating_books.shared.infrastructure.responses.BookResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminBookControllerTest extends IntegrationTestCase {

    @Test
    void test_post_controller() throws Exception {
        MvcResult result = mockMvc.perform(createRequestPost()).andExpect(status().isOk()).andReturn();

        List<AuditLogEntity> logs = auditRepository.findAll().stream().filter(
                audit -> audit.getEntityType().equals(Entities.BOOK.name())
        ).toList();
        assertNotNull(result.getResponse().getContentAsString());
        assertThat(logs.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void test_update_controller_200() throws Exception {
        Book book = bookRepository.save(BookMother.random());
        MvcResult result = mockMvc.perform(createRequestPut(book)).andExpect(status().isOk()).andReturn();
        assertNotNull(result.getResponse().getContentAsString());

        BookResponse response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookResponse.class);

        assertThat(book.getPublicationDate().toString()).isNotEqualTo(response.publicationDate());
        assertThat(book.getEditorial().getValue()).isEqualTo(response.editorial());
        assertThat(book.getAuthor().getValue()).isEqualTo(response.author());
    }

    private MockHttpServletRequestBuilder createRequestPost() throws JsonProcessingException {
        return post("/admin/books").header("Authorization", basicAuthAdmin())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createPostBodyRequest())
                .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    private MockHttpServletRequestBuilder createRequestPut(Book book) throws JsonProcessingException {
        return put("/admin/books/update").header("Authorization", basicAuthAdmin())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createPutBodyRequest(book))
                .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    private String createPutBodyRequest(Book book) throws JsonProcessingException {
        AdminBookController.UpdateBookRequest request = new AdminBookController.UpdateBookRequest(
                book.getId().getValue(),
                null,
                "1987-12-12");
        return objectMapper.writeValueAsString(request);
    }

    private String createPostBodyRequest() throws JsonProcessingException {
        Book example = BookMother.random();
        AdminBookController.CreateBookRequest request = new AdminBookController.CreateBookRequest(
                example.getTitle().getValue(),
                example.getAuthor().getValue(),
                example.getEditorial().getValue(),
                example.getIsbn().getValue(),
                example.getPublicationDate().toString());
        return objectMapper.writeValueAsString(request);
    }

}