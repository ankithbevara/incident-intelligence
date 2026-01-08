package com.incident.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRuleRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String serviceName;

    @NotBlank
    private String eventType; // ERROR

    @Min(1)
    private int threshold; // 5

    @Min(1)
    private int windowSeconds; // 30

    @NotBlank
    private String severity; // CRITICAL

    @Min(0)
    private int cooldownSeconds; // 120 (0 allowed)

    private boolean enabled = true;
}
