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

    @Test
    void info_whenCalled_shouldReturnApplicationInfo() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(get("/api/info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.application.name").value("n8n-borisevich-test"))
            .andExpect(jsonPath("$.application.version").exists())
            .andExpect(jsonPath("$.application.description").value("Spring Boot Application"))
            .andExpect(jsonPath("$.java.version").exists())
            .andExpect(jsonPath("$.java.vendor").exists())
            .andExpect(jsonPath("$.java.runtime").exists())
            .andExpect(jsonPath("$.system.os").exists())
            .andExpect(jsonPath("$.system.architecture").exists());
    }

    @Test
    void info_whenCalled_shouldReturnJsonContentType() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(get("/api/info"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void info_whenCalledWithPost_shouldReturnMethodNotAllowed() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(post("/api/info"))
            .andExpect(status().isMethodNotAllowed());
    }
}
