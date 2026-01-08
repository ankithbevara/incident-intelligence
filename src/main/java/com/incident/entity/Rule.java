package com.incident.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rules")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Rule {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String eventType; // ERROR, WARN, etc.

    @Column(nullable = false)
    private int threshold; // e.g., 5 number of events failing the rule condition to trigger incident 

    @Column(nullable = false)
    private int windowSeconds; // e.g., 30, this is to check when event happened in last X seconds- used to catch spikes

    @Column(nullable = false)
    private String severity; // CRITICAL/HIGH

    @Column(nullable = false)
    private int cooldownSeconds; // e.g., 120, this is about noise control, we dont need incident every time the rule triggers. so after triggering, we wait for cooldown period before triggering again- used to prevent alert spam

    @Column(nullable = false)
    private boolean enabled;

    // Used to prevent alert spam
    private Instant lastTriggeredAt;

    private Instant createdAt;
}
