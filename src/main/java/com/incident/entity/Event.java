package com.incident.entity;

import jakarta.persistence.*; //to import JPA annotations like @Entity, @id, @column, @Table..
import lombok.*;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import tools.jackson.databind.JsonNode; //store metadata as JSON by representing it as an object tree

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue
    private UUID id; //PRIMARY KEY, used to generate value automatically

    @Column(nullable = false)
    private String serviceName; 

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @JdbcTypeCode(SqlTypes.JSON) //store this field as a JSON column.
    @Column(columnDefinition = "jsonb")
    private JsonNode metadata;

    private Instant occurredAt;

    private Instant ingestedAt;

    /*
        // Without Lombok:
        // REQUIRED empty constructor by JPA
        public Event() {
        }

        // Optional full constructor (useful in tests)
        public Event(String serviceName, String eventType, String severity, String message, String metadata, Instant occurredAt, Instant ingestedAt) {
            this.serviceName = serviceName;
            this.eventType = eventType;
            this.severity = severity;
            this.message = message;
            this.metadata = metadata;
            this.occurredAt = occurredAt;
            this.ingestedAt = ingestedAt;
        }

        // Getters & Setters

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public JsonNode getMetadata() {
            return metadata;
        }

        public void setMetadata(JsonNode metadata) {
            this.metadata = metadata;
        }

        public Instant getOccurredAt() {
            return occurredAt;
        }

        public void setOccurredAt(Instant occurredAt) {
            this.occurredAt = occurredAt;
        }

        public Instant getIngestedAt() {
            return ingestedAt;
        }

        public void setIngestedAt(Instant ingestedAt) {
            this.ingestedAt = ingestedAt;
        }
    */
}