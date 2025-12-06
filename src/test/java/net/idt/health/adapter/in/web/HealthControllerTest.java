package net.idt.health.adapter.in.web;

import net.idt.health.application.port.in.HealthQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthQuery healthQuery;

    @Test
    void health_whenCalled_shouldReturnOk() throws Exception {
        //Given
        when(healthQuery.getHealth()).thenReturn(Map.of(
                "status", "UP",
                "message", "Application is running"
        ));
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
        when(healthQuery.getHealth()).thenReturn(Map.of(
                "status", "UP",
                "message", "Application is running"
        ));
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
        when(healthQuery.getInfo()).thenReturn(Map.of(
                "application", Map.of(
                        "name", "n8n-borisevich-test",
                        "version", "1.0.0",
                        "description", "Spring Boot Application"
                ),
                "java", Map.of(
                        "version", "21.0.1",
                        "vendor", "Oracle Corporation",
                        "runtime", "Java(TM) SE Runtime Environment"
                ),
                "system", Map.of(
                        "os", "Windows 11 10.0",
                        "architecture", "amd64"
                )
        ));
        //When
        //Then
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application.name").value("n8n-borisevich-test"))
                .andExpect(jsonPath("$.application.version").value("1.0.0"))
                .andExpect(jsonPath("$.application.description").value("Spring Boot Application"))
                .andExpect(jsonPath("$.java.version").value("21.0.1"))
                .andExpect(jsonPath("$.java.vendor").value("Oracle Corporation"))
                .andExpect(jsonPath("$.java.runtime").value("Java(TM) SE Runtime Environment"))
                .andExpect(jsonPath("$.system.os").value("Windows 11 10.0"))
                .andExpect(jsonPath("$.system.architecture").value("amd64"));
    }

    @Test
    void info_whenCalled_shouldReturnJsonContentType() throws Exception {
        //Given
        when(healthQuery.getInfo()).thenReturn(Map.of());
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
