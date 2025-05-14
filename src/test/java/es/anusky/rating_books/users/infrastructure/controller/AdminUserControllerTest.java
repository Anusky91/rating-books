package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.shared.infrastructure.responses.UserResponse;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminUserControllerTest extends IntegrationTestCase {

    @Test
    void test_findAll() throws Exception {
        List<String> aliasSaved = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = UserMother.random();
            userRepository.save(user);
            aliasSaved.add(user.getAlias().getValue());
        }
        MvcResult result = mockMvc.perform(get("/admin/users")
                        .header("Authorization", basicAuth())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
        List<String> aliasRecovered = new ArrayList<>();
        UserResponse[] users = objectMapper.readValue(result.getResponse().getContentAsByteArray(), UserResponse[].class);

        for (UserResponse u : users) {
            aliasRecovered.add(u.alias());
        }
        assertNotNull(result.getResponse().getContentAsString());
        assertThat(users.length).isGreaterThanOrEqualTo(10);
        assertThat(aliasRecovered).containsAll(aliasSaved);
    }

    @Test
    void test_findById() throws Exception {

        User user = userRepository.save(UserMother.random());

        MvcResult result = mockMvc.perform(get("/admin/users/" + user.getUserId().getValue())
                        .header("Authorization", basicAuth())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        UserResponse response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), UserResponse.class);

        assertThat(response).isNotNull();
        assertEquals(user.getEmail().getValue(), response.email());
    }

    @Test
    void test_findByAlias() throws Exception {

        User user = userRepository.save(UserMother.random());

        MvcResult result = mockMvc.perform(get("/admin/users/alias/" + user.getAlias().getValue())
                        .header("Authorization", basicAuth())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        UserResponse response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), UserResponse.class);

        assertThat(response).isNotNull();
        assertEquals(user.getEmail().getValue(), response.email());
    }

    @Test
    void test_findByEmail() throws Exception {

        User user = userRepository.save(UserMother.random());

        MvcResult result = mockMvc.perform(get("/admin/users/email/" + user.getEmail().getValue())
                        .header("Authorization", basicAuth())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        UserResponse response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), UserResponse.class);

        assertThat(response).isNotNull();
        assertEquals(user.getEmail().getValue(), response.email());
    }

    @Test
    void test_findByAlias_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/admin/users/alias/aliasInexistente").header("Authorization", basicAuth()))
                .andExpect(status().isNotFound());
    }


}