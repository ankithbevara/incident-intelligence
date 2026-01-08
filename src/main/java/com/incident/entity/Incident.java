package com.incident.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "incidents")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Incident {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID ruleId;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private String status; // OPEN, ACKED, RESOLVED

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false)
    private Instant triggeredAt;

    private Instant acknowledgedAt;
    private String acknowledgedBy;

    private Instant resolvedAt;
    private String resolvedBy;
}