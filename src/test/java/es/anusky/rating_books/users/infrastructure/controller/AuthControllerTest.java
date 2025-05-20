package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends IntegrationTestCase {

    @Test
    void test_post_return_200() throws Exception {
        String password = "Prueba123!";
        User user = userRepository.create(UserMother.with(password, true, false)).getFirst();

        AuthController.AuthRequest request = new AuthController.AuthRequest(user.getAlias().getValue(), password);

        MvcResult result = mockMvc.perform(post("/auth/check")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        AuthController.AuthStatusResponse response = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                AuthController.AuthStatusResponse.class);

        assertThat(response.status()).isEqualTo("ACTIVE");
    }

    @Test
    void test_post_return_401() throws Exception {
        String password = "Prueba123!";
        User user = userRepository.create(UserMother.with(password, true, false)).getFirst();

        AuthController.AuthRequest request = new AuthController.AuthRequest(user.getAlias().getValue(), "Prueba13!");

        MvcResult result = mockMvc.perform(post("/auth/check")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Usuario o contraseña incorrecto");
    }

    @Test
    void test_post_return_403() throws Exception {
        String password = "Prueba123!";
        User user = userRepository.create(UserMother.with(password, true, true)).getFirst();

        AuthController.AuthRequest request = new AuthController.AuthRequest(user.getAlias().getValue(), password);

        MvcResult result = mockMvc.perform(post("/auth/check")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn();

        AuthController.AuthStatusResponse response = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                AuthController.AuthStatusResponse.class);

        assertThat(response.status()).isEqualTo("LOCKED");
    }

}