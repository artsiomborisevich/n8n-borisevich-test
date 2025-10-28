package net.idt.health.adapter.in.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Value("${spring.application.name}")
    private String applicationName;

    private final Optional<BuildProperties> buildProperties;

    public HealthController(Optional<BuildProperties> buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "message", "Application is running"
        ));
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("application", Map.of(
            "name", applicationName,
            "version", buildProperties.map(BuildProperties::getVersion).orElse("unknown"),
            "description", "Spring Boot Application"
        ));
        info.put("java", Map.of(
            "version", System.getProperty("java.version"),
            "vendor", System.getProperty("java.vendor"),
            "runtime", System.getProperty("java.runtime.name")
        ));
        info.put("system", Map.of(
            "os", System.getProperty("os.name") + " " + System.getProperty("os.version"),
            "architecture", System.getProperty("os.arch")
        ));

        return ResponseEntity.ok(info);
    }
}
