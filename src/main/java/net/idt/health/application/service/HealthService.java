package net.idt.health.application.service;

import net.idt.health.application.port.in.HealthQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class HealthService implements HealthQuery {

    @Value("${spring.application.name}")
    private String applicationName;

    private final Optional<BuildProperties> buildProperties;

    public HealthService(Optional<BuildProperties> buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Override
    public Map<String, String> getHealth() {
        return Map.of(
            "status", "UP",
            "message", "Application is running"
        );
    }

    @Override
    public Map<String, Object> getInfo() {
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

        return info;
    }
}
