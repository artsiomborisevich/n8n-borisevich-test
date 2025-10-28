package net.idt.health.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void health_whenCalled_shouldReturnOk() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(get("/api/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.message").value("Application is running"));
    }

    @Test
    void health_whenCalled_shouldReturnJsonContentType() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(get("/api/health"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void health_whenCalledWithPost_shouldReturnMethodNotAllowed() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(post("/api/health"))
            .andExpect(status().isMethodNotAllowed());
    }
}
