package es.anusky.rating_books.users.infrastructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.shared.infrastructure.responses.UserResponse;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
class PublicUserControllerTest extends IntegrationTestCase {

    @Test
    void test_create_controller() throws Exception {
        User expected = UserMother.random();

        MvcResult result = mockMvc.perform(createRequestPost(expected)).andExpect(status().isOk()).andReturn();
        UserResponse actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), UserResponse.class);

        assertNotNull(result.getResponse().getContentAsString());
        assertEquals(expected.getAlias().getValue(), actual.alias());
        assertEquals(expected.getEmail().getValue(), actual.email());
    }

    @Test
    void test_create_controller_exception_400() throws Exception {

        MvcResult result = mockMvc.perform(createBadRequestPost()).andExpect(status().is4xxClientError()).andReturn();

        assertNotNull(result.getResponse().getContentAsString());
        assertThat(result.getResponse().getContentAsString()).contains("lastName: Los apellidos son obligatorios");
    }

    private MockHttpServletRequestBuilder createRequestPost(User user) throws JsonProcessingException {
        return post("/public/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createPostBodyRequest(user))
                .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    private String createPostBodyRequest(User user) throws JsonProcessingException {
        PublicUserController.CreateUserRequest request = new PublicUserController.CreateUserRequest(user.getFirstName().getValue(),
                user.getLastName().getValue(),
                user.getAlias().getValue(),
                user.getEmail().getValue(),
                user.getPhoneNumber().getValue(),
                user.getPassword().getValue(),
                user.getCountry().getCode(),
                user.getBirthDate().toString(),
                user.getRole().toString(),
                user.getAvatarUrl());
        return objectMapper.writeValueAsString(request);
    }

    private MockHttpServletRequestBuilder createBadRequestPost() throws JsonProcessingException {
        return post("/public/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createBadPostBodyRequest())
                .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    private String createBadPostBodyRequest() throws JsonProcessingException {
        PublicUserController.CreateUserRequest request = new PublicUserController.CreateUserRequest("Ana",
                "",
                "anita",
                "anita@gmail",
                "665431243",
                "fadf2334",
                "ES",
                "1990-12-40",
                "USER",
                "");
        return objectMapper.writeValueAsString(request);
    }

}