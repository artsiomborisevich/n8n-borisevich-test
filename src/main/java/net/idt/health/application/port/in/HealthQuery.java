package net.idt.health.application.port.in;

import java.util.Map;

public interface HealthQuery {
    Map<String, String> getHealth();
    Map<String, Object> getInfo();
}
