package com.incident.dto;

import jakarta.validation.constraints.NotBlank; //Validation rules
import lombok.Data;

import java.time.Instant;

import tools.jackson.databind.JsonNode;

@Data //4-> to create getters, setters, toString() etc automatically
public class EventRequest {

    //UUID, automatically created in backend, client cannot create manual UUIDs (security, tight coupling issues)

    @NotBlank
    private String serviceName;

    @NotBlank
    private String eventType;

    @NotBlank
    private String severity;

    @NotBlank
    private String message;

    private JsonNode metadata;

    private Instant occurredAt;

    //ingested time needed at backend, Not required for client


    /*
        // Without Lombok:
        // REQUIRED empty constructor by JPA
        public EventRequest() {
        }

        // Optional full constructor (useful in tests)
        public EventRequest(String serviceName, String eventType, String severity, String message, String metadata, Instant occurredAt) {
            this.serviceName = serviceName;
            this.eventType = eventType;
            this.severity = severity;
            this.message = message;
            this.metadata = metadata;
            this.occurredAt = occurredAt;

        }

        // Getters & Setters

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
    */
}