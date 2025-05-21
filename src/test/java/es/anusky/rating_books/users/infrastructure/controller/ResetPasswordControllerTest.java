package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import es.anusky.rating_books.infrastructure.MailHogHelper;
import es.anusky.rating_books.infrastructure.TokenExtractor;
import es.anusky.rating_books.users.domain.model.User;
import es.anusky.rating_books.users.domain.model.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
class ResetPasswordControllerTest extends IntegrationTestCase {

    @Test
    void test_init_recover_with_existing_user() throws Exception {
        Pair<MvcResult, User> results = testResetHappyPath();

        assertThat(results.getFirst().getResponse().getContentAsString()).isEqualTo("Ok");
        Thread.sleep(500);

        assertThat(MailHogHelper.containsEmailTo(results.getSecond().getEmail().getValue())).isTrue();

    }

    @Test
    void test_init_recover_without_existing_user() throws Exception {
        String alias = "NoValidUsername";
        ResetPasswordController.RecoverPasswordRequest request = new ResetPasswordController.RecoverPasswordRequest(alias);

        MvcResult result = mockMvc.perform(post("/auth/recover")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Ok");
        Thread.sleep(500);

        assertThat(MailHogHelper.containsAliasOnBody(alias)).isFalse();

    }

    @Test
    void test_reset_password_200() throws Exception {
        Pair<MvcResult, User> results = testResetHappyPath();

        assertThat(results.getFirst().getResponse().getContentAsString()).isEqualTo("Ok");
        Thread.sleep(500);
        Optional<String> emailBody = MailHogHelper.getEmailBody(results.getSecond().getEmail().getValue());
        String token = TokenExtractor.extract(emailBody.orElse(""));

        ResetPasswordController.ResetPasswordRequest resetRequest = new ResetPasswordController.ResetPasswordRequest(token, "LaLoLa12!+!+");
        MvcResult mvcResult = mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(resetRequest)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Contraseña restaurada con exito");
        User updated = userRepository.findByAlias(results.getSecond().getAlias().getValue()).orElseThrow();
        assertThat(updated.isLocked()).isFalse();

    }

    @Test
    void test_reset_password_404() throws Exception {
        Pair<MvcResult, User> results = testResetHappyPath();

        assertThat(results.getFirst().getResponse().getContentAsString()).isEqualTo("Ok");
        Thread.sleep(500);
        String token = UUID.randomUUID().toString();

        ResetPasswordController.ResetPasswordRequest resetRequest = new ResetPasswordController.ResetPasswordRequest(token, "LaLoLa12!+!+");
        MvcResult mvcResult = mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(resetRequest)))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("{\"status\":\"404 NOT_FOUND\",\"error\":\"Token not found\"}");
    }

    @Test
    void test_reset_password_token_already_used() throws Exception {
        // flujo completo
        Pair<MvcResult, User> results = testResetHappyPath();
        Thread.sleep(500);

        String token = TokenExtractor.extract(
                MailHogHelper.getEmailBody(results.getSecond().getEmail().getValue()).orElseThrow()
        );

        // primer reset (válido)
        ResetPasswordController.ResetPasswordRequest resetRequest =
                new ResetPasswordController.ResetPasswordRequest(token, "LaLoLa12!+!+");

        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(resetRequest)))
                .andExpect(status().isOk());

        // segundo reset con el mismo token (ya usado)
        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(resetRequest)))
                .andExpect(status().isConflict());
    }


    private Pair<MvcResult, User> testResetHappyPath() throws Exception {
        User user = userRepository.create(UserMother.with("LaLa123**++!", true, false)).getFirst();
        ResetPasswordController.RecoverPasswordRequest request = new ResetPasswordController.RecoverPasswordRequest(user.getAlias().getValue());

        MvcResult result = mockMvc.perform(post("/auth/recover")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        return Pair.of(result, user);
    }

}