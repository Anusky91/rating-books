package es.anusky.rating_books.infrastructure.controller;

import es.anusky.rating_books.infrastructure.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
class HealthCheckControllerTest extends IntegrationTestCase {

    @Test
    void test_health_check() throws Exception {
        MvcResult result = mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("{\"name\":\"BookStar\",\"status\":\"alive\"}");
    }

    @Test
    void test_health_check_exception() throws Exception {
        MvcResult result = mockMvc.perform(get("/healt"))
                .andExpect(status().is5xxServerError())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("{\"status\":\"500 INTERNAL_SERVER_ERROR\",\"error\":\"Ha ocurrido un error inesperado\"}");
    }

}