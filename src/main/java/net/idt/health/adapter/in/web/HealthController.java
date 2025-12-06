package net.idt.health.adapter.in.web;

import net.idt.health.application.port.in.HealthQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final HealthQuery healthQuery;

    public HealthController(HealthQuery healthQuery) {
        this.healthQuery = healthQuery;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(healthQuery.getHealth());
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(healthQuery.getInfo());
    }
}
