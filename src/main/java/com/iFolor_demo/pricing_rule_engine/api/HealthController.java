package com.iFolor_demo.pricing_rule_engine.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${app.api.version:v1}")
@Tag(name = "Health", description = "Service health endpoints")
public class HealthController {

    private final String apiVersion;

    public HealthController(@Value("${app.api.version:v1}") String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @GetMapping("/health")
    @Operation(
        summary = "Health check",
        description = "Returns service health status for the configured API version"
    )
    public HealthResponse health() {
        return new HealthResponse("UP", "pricing-rule-engine", apiVersion);
    }

    public record HealthResponse(
        String status,
        String service,
        String version
    ) {}
}
